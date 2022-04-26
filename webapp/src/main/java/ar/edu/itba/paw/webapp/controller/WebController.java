package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.DebateCategory;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;

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
    public ModelAndView debatesList(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "page", defaultValue = "0") int page) {
        final ModelAndView mav = new ModelAndView("pages/debates-list");
        mav.addObject("search", search);
        mav.addObject("categories", DebateCategory.values());
        mav.addObject("total_pages", (int) Math.ceil( (double) (debateService.getCount(search) / 10)));
        mav.addObject("debates", debateService.get(page, search));
        return mav;
    }

    @RequestMapping(value = "/debates/category/{category}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debatesCategoryList(@PathVariable("category") String category, @RequestParam(value = "page", defaultValue = "0") int page) {
        final ModelAndView mav = new ModelAndView("pages/debates-list");
        mav.addObject("total_pages", (int) Math.ceil( (double) (debateService.getFromCategoryCount(DebateCategory.valueOf(category.toUpperCase())) / 15)));
        mav.addObject("currentCategory", category);
        mav.addObject("categories", DebateCategory.values());
        mav.addObject("debates", debateService.getFromCategory(DebateCategory.valueOf(category.toUpperCase()),page));
        return mav;
    }

    @RequestMapping(value = "/debates/{debateId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debate(@PathVariable("debateId") final long debateId, @ModelAttribute("postForm") final PostForm form, @RequestParam(value = "page", defaultValue = "0") int page) {
        final ModelAndView mav = new ModelAndView("pages/debate");
        mav.addObject("debate", debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new));
        mav.addObject("total_pages", (int) Math.ceil( (double) (postService.getPostsByDebateCount(debateId) / 15)));
        mav.addObject("posts", postService.getPublicPostsByDebate(debateId, page));
        return mav;
    }

    @RequestMapping(value = "/debates/{debateId}", method = { RequestMethod.POST })
    public ModelAndView createPost(@PathVariable("debateId") final long debateId, @Valid @ModelAttribute("postForm") final PostForm form, BindingResult errors, Authentication auth) throws IOException {
        if (errors.hasErrors()) {
            return debate(debateId, form, 0);
        }
        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
        postService.create(user.getUserId(), debateId, form.getContent(), form.getFile().getBytes());
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
    public ModelAndView profilePage(@ModelAttribute("profileImageForm") final ProfileImageForm form, Authentication auth, @RequestParam(value = "page", defaultValue = "0") int page) {
        final ModelAndView mav = new ModelAndView("/pages/profile");

        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
        mav.addObject("user", user);
        mav.addObject("total_pages", (int) Math.ceil( (double) (debateService.getSubscribedDebatesByUsernameCount(user.getUserId()) / 5)));
        mav.addObject("suscribed_debates", debateService.getSubscribedDebatesByUsername(user.getUserId(), page));
        return mav;
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.POST})
    public ModelAndView editProfileImage(@Valid @ModelAttribute("profileImageForm") final ProfileImageForm form, BindingResult errors, Authentication auth) throws IOException {
        if(errors.hasErrors()) {
            LOGGER.info("Error uploading file {}", errors);
            return profilePage(form, auth, 0);
        }

        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);

        userService.updateImage(user.getUserId(), form.getFile().getBytes());

        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/create_debate", method = { RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView createDebatePage() {
        return new ModelAndView("pages/login");
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public byte[] getImage(@PathVariable("imageId") final long imageId) {
        Image image = imageService.getImage(imageId).orElseThrow(ImageNotFoundException::new);
        return image.getData();
    }

    @RequestMapping(value = "/create-debate", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView createDebatePage(@ModelAttribute("createDebateForm") final CreateDebateForm form) {
        return new ModelAndView("pages/create-debate");
    }

    @RequestMapping(value = "/create-debate", method = { RequestMethod.POST })
    public ModelAndView createDebate(@Valid @ModelAttribute("createDebateForm") final CreateDebateForm form, BindingResult errors) throws IOException {
        if (errors.hasErrors()) {
            return createDebatePage(form);
        }
//        userService.create(form.getTitle(), form.getDescription(), form.getCategoryId(), form.getImage().getBytes());
        return new ModelAndView("redirect:/");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserNotFoundException(UserAlreadyExistsException e) {
        ModelAndView mav = new ModelAndView("pages/register");
        mav.addObject("registerForm", new RegisterForm());
        mav.addObject("userNotFound", e);
        return mav;
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleImageNotFoundException() {
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(DebateNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleUserNotFoundException() {
        return new ModelAndView("error/404");
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
