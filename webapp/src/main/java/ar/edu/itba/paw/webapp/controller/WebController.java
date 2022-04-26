package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;

@Controller
public class WebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);
    private final UserService userService;
    private final DebateService debateService;
    private final PostService postService;
    private final ImageService imageService;

    @Autowired
    public WebController(UserService userService, DebateService debateService, PostService postService, ImageService imageService) {
        this.userService = userService;
        this.debateService = debateService;
        this.postService = postService;
        this.imageService = imageService;
    }
    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("pages/landing-page");
        mav.addObject("debates", debateService.getMostSubscribed());
        mav.addObject("categories", DebateCategory.values());
        return mav;
    }

    @RequestMapping(value = "/debates", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debatesList(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "page", defaultValue = "0") String page) {
        if (!page.matches("\\d+")) throw new DebateNotFoundException();
        final ModelAndView mav = new ModelAndView("pages/debates-list");
        mav.addObject("search", search);
        mav.addObject("categories", DebateCategory.values());
        mav.addObject("total_pages", (int) Math.ceil( (double) (debateService.getCount(search) / 10)));
        mav.addObject("debates", debateService.get(Integer.parseInt(page), search));
        return mav;
    }

    @RequestMapping(value = "/debates/category/{category}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debatesCategoryList(@PathVariable("category") String category, @RequestParam(value = "page", defaultValue = "0") String page) {
        if (!page.matches("\\d+")) throw new DebateNotFoundException();
        if (Arrays.stream(DebateCategory.values()).noneMatch((c) -> c.getName().equals(category))) throw new CategoryNotFoundException();
        final ModelAndView mav = new ModelAndView("pages/debates-list");
        mav.addObject("total_pages", (int) Math.ceil( (double) (debateService.getFromCategoryCount(DebateCategory.valueOf(category.toUpperCase())) / 15)));
        mav.addObject("currentCategory", category);
        mav.addObject("categories", DebateCategory.values());
        mav.addObject("debates", debateService.getFromCategory(DebateCategory.valueOf(category.toUpperCase()),Integer.parseInt(page)));
        return mav;
    }

    @RequestMapping(value = "/debates/{debateId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debate(@PathVariable("debateId") final String debateId, @ModelAttribute("postForm") final PostForm form, @RequestParam(value = "page", defaultValue = "0") String page) {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        if (!page.matches("\\d+")) throw new PostNotFoundException();
        long id = Long.parseLong(debateId);
        final ModelAndView mav = new ModelAndView("pages/debate");
        mav.addObject("debate", debateService.getPublicDebateById(id).orElseThrow(DebateNotFoundException::new));
        mav.addObject("total_pages", (int) Math.ceil( (double) (postService.getPostsByDebateCount(id) / 15)));
        mav.addObject("posts", postService.getPublicPostsByDebate(id, Integer.parseInt(page)));
        return mav;
    }

    @RequestMapping(value = "/debates/{debateId}", method = { RequestMethod.POST })
    public ModelAndView createPost(@PathVariable("debateId") final String debateId, @Valid @ModelAttribute("postForm") final PostForm form, BindingResult errors, Authentication auth) throws IOException {
        if (!debateId.matches("\\d+")) throw new DebateNotFoundException();
        long id = Long.parseLong(debateId);
        if (errors.hasErrors()) {
            return debate(debateId, form, "0");
        }
        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
        postService.create(user.getUserId(), id, form.getContent(), form.getFile().getBytes());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/moderator", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView moderatorPage(@ModelAttribute("moderatorForm") final ModeratorForm form) {
        return new ModelAndView("pages/request-moderator");
    }

    @RequestMapping(value = "/moderator", method = { RequestMethod.POST })
    public ModelAndView moderatorPage(@Valid @ModelAttribute("moderatorForm") final ModeratorForm form, BindingResult errors, Authentication authentication) {
        if (errors.hasErrors()) {
            return moderatorPage(form);
        }
        LOGGER.info("user {} requested moderator status because reason: {}", authentication.getName(), form.getReason());
        userService.requestModerator(authentication.getName(), form.getReason());
        return new ModelAndView("redirect:/debates");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView loginPage(@RequestParam(value = "error", required = false) final String error) {
        LOGGER.error("error logging in: {}", error);
        return new ModelAndView("pages/login");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView registerPage(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("pages/register");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute("registerForm") final RegisterForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            LOGGER.info("Error registering new user {}", errors);
            return registerPage(form);
        }
        userService.create(form.getUsername(), form.getPassword(), form.getEmail());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView profilePage(@ModelAttribute("profileImageForm") final ProfileImageForm form, Authentication auth, @RequestParam(value = "page", defaultValue = "0") String page) {
        if (!page.matches("\\d+")) throw new DebateNotFoundException();

        final ModelAndView mav = new ModelAndView("/pages/profile");

        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
        mav.addObject("user", user);
        mav.addObject("total_pages", (int) Math.ceil( (double) (debateService.getSubscribedDebatesByUsernameCount(user.getUserId()) / 5)));
        mav.addObject("subscribed_debates", debateService.getSubscribedDebatesByUsername(user.getUserId(), Integer.parseInt(page)));
        return mav;
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.POST})
    public ModelAndView editProfileImage(@Valid @ModelAttribute("profileImageForm") final ProfileImageForm form, BindingResult errors, Authentication auth) throws IOException {
        if(errors.hasErrors()) {
            LOGGER.info("Error uploading file {}", errors);
            return profilePage(form, auth, "0");
        }

        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);

        userService.updateImage(user.getUserId(), form.getFile().getBytes());

        return new ModelAndView("redirect:/profile");
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public byte[] getImage(@PathVariable("imageId") final long imageId) {
        Image image = imageService.getImage(imageId).orElseThrow(ImageNotFoundException::new);
        return image.getData();
    }

    @RequestMapping(value = "/create_debate", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView createDebatePage(@ModelAttribute("createDebateForm") final CreateDebateForm form) {
        ModelAndView mav = new ModelAndView("pages/create-debate");
        mav.addObject("categories", DebateCategory.values());
        return mav;
    }

    @RequestMapping(value = "/create_debate", method = { RequestMethod.POST })
    public ModelAndView createDebate(@Valid @ModelAttribute("createDebateForm") final CreateDebateForm form, BindingResult errors, Authentication auth) throws IOException {
        if (errors.hasErrors()) {
            return createDebatePage(form);
        }
        debateService.create(form.getTitle(),
                form.getDescription(),
                auth.getName(),
                form.getOpponentUsername(),
                form.getImage().getBytes(),
                form.getCategory());
        return new ModelAndView("redirect:/debates");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView UserAlreadyExistsException(UserAlreadyExistsException e) {
        ModelAndView mav = new ModelAndView("pages/register");
        RegisterForm form = new RegisterForm();
        form.setEmail(e.getEmail());
        form.setUsername(e.getUsername());
        form.setPassword(e.getPassword());
        form.setPasswordConfirmation(e.getPassword());
        mav.addObject("registerForm", form);
        mav.addObject("userAlreadyExists", e);
        return mav;
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleImageNotFoundException() {
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handlePostNotFoundException() {
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleCategoryNotFoundException() {
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(DebateNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleDebateNotFoundException() {
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(DebateOponentException.class)
    public ModelAndView handleDebateOponentException(DebateOponentException e) {
        ModelAndView mav = new ModelAndView("pages/create-debate");
        CreateDebateForm form = new CreateDebateForm();
        form.setTitle(e.getDebateTitle());
        form.setDescription(e.getDebateDescription());
        form.setCategory(e.getDebateCategory());
        form.setOpponentUsername(e.getOponent());
        mav.addObject("createDebateForm", form);
        mav.addObject("debateOponentException", e);
        mav.addObject("categories", DebateCategory.values());
        return mav;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException() {
        return new ModelAndView("redirect:/logout");
    }

    @ExceptionHandler(ForbiddenPostException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView handleForbiddenPostException() {
        return error403();
    }

    @RequestMapping(value = "/404", method = { RequestMethod.GET})
    public ModelAndView error() {
        return new ModelAndView("error/404");
    }
    // TODO: implement 403 error page
    @RequestMapping(value = "/403", method = { RequestMethod.GET})
    public ModelAndView error403() {
        return new ModelAndView("error/404");
    }
}
