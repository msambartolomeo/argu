package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.exceptions.*;
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
    private final ImageService imageService;

    @Autowired
    public WebController(UserService userService, DebateService debateService, ImageService imageService) {
        this.userService = userService;
        this.debateService = debateService;
        this.imageService = imageService;
    }
    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("pages/landing-page");
        mav.addObject("debates", debateService.getMostSubscribed());
        mav.addObject("categories", DebateCategory.values());
        return mav;
    }

    @RequestMapping(value = "/moderator", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView moderatorPage(@ModelAttribute("moderatorForm") final ModeratorForm form) {
        return new ModelAndView("pages/request-moderator");
    }

    @RequestMapping(value = "/moderator", method = { RequestMethod.POST })
    public ModelAndView moderatorPage(@Valid @ModelAttribute("moderatorForm") final ModeratorForm form, BindingResult errors, Authentication authentication) {
        if (errors.hasErrors()) {
            LOGGER.warn("Moderator page form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return moderatorPage(form);
        }
        LOGGER.info("user {} requested moderator status because reason: {}", authentication.getName(), form.getReason());
        userService.requestModerator(authentication.getName(), form.getReason());
        return new ModelAndView("redirect:/debates");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView loginPage(@RequestParam(value = "error", required = false) final String error) {
        return new ModelAndView("pages/login");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView registerPage(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("pages/register");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView register(@Valid @ModelAttribute("registerForm") final RegisterForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            LOGGER.warn("Register form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return registerPage(form);
        }
        userService.create(form.getUsername(), form.getPassword(), form.getEmail());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView profilePage(@ModelAttribute("profileImageForm") final ProfileImageForm form, @ModelAttribute("confirmationModal") final ConfirmationForm userForm, Authentication auth, @RequestParam(value = "list", defaultValue = "subscribed") String list, @RequestParam(value = "page", defaultValue = "0") String page) {
        if (!page.matches("-?\\d+")) {
            LOGGER.error("/profile : Invalid page number {}", page);
            throw new InvalidPageException();
        }

        final ModelAndView mav = new ModelAndView("/pages/profile");

        if (!list.equals("subscribed") && !list.equals("mydebates"))
            list = "subscribed";

        User user = userService.getUserByUsername(auth.getName()).orElseThrow(() -> {
            LOGGER.error("/profile : User {} not found", auth.getName());
            return new UserNotFoundException();
        });
        mav.addObject("user", user);
        mav.addObject("total_pages", debateService.getProfileDebatesPageCount(list, user.getUserId()));
        mav.addObject("debates", debateService.getProfileDebates(list, user.getUserId(), Integer.parseInt(page)));
        return mav;
    }

    @RequestMapping(value = "/user/{username}", method = { RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView userProfile(@PathVariable("username") final String username, Authentication auth, @RequestParam(value = "page", defaultValue = "0") String page) {
        if (!page.matches("-?\\d+")) {
            LOGGER.error("/user/{username} : Invalid page number {}", page);
            throw new InvalidPageException();
        }

        if (auth != null && auth.getPrincipal() != null && auth.getName().equals(username)) {
            return new ModelAndView("redirect:/profile");
        }

        final ModelAndView mav = new ModelAndView("/pages/user_profile");
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("/user/{username} : User {} not found", username);
            return new UserNotFoundException();
        });
        mav.addObject("user", user);
        mav.addObject("total_pages", debateService.getUserDebatesPageCount(user.getUserId()));
        mav.addObject("debates", debateService.getUserDebates(user.getUserId(), Integer.parseInt(page)));
        return mav;
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.POST}, params = "editImage")
    public ModelAndView editProfileImage(@ModelAttribute("confirmationModal") final ConfirmationForm confirmationForm, @Valid @ModelAttribute("profileImageForm") final ProfileImageForm form, BindingResult errors, Authentication auth) throws IOException {
        if(errors.hasErrors()) {
            LOGGER.warn("Profile image form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return profilePage(form, confirmationForm, auth, "subscribed", "0");
        }

        userService.updateImage(auth.getName(), form.getFile().getBytes());
        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.POST}, params = "deleteAccount")
    public ModelAndView deleteUser(@ModelAttribute("profileImageForm") final ProfileImageForm imageForm, @Valid @ModelAttribute("confirmationModal") final ConfirmationForm form, BindingResult errors, Authentication auth) {
        if(errors.hasErrors()) {
            LOGGER.warn("Confirmation form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return profilePage(imageForm, form, auth, "subscribed", "0");
        }

        userService.deleteUser(auth.getName());
        return new ModelAndView("redirect:/logout");
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageId:\\d+}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public byte[] getImage(@PathVariable("imageId") final String imageId) {
        return imageService.getImage(Integer.parseInt(imageId)).orElseThrow(() -> {
            LOGGER.error("/images/{imageId} : Image {} not found", imageId);
            return new ImageNotFoundException();
        }).getData();
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
            LOGGER.warn("Create debate form has {} errors: {}", errors.getErrorCount(), errors.getAllErrors());
            return createDebatePage(form);
        }

        debateService.create(form.getTitle(),
                form.getDescription(),
                auth.getName(),
                form.getIsCreatorFor(),
                form.getOpponentUsername(),
                form.getImage().getBytes(),
                form.getCategory());
        return new ModelAndView("redirect:/debates");
    }

    @RequestMapping(value = "/404")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView error() {
        LOGGER.error("error 404");
        return new ModelAndView("error/404");
    }

    @RequestMapping(value = "/500")
    public ModelAndView error500() {
        LOGGER.error("error 500");
        return new ModelAndView("error/500"); }
}
