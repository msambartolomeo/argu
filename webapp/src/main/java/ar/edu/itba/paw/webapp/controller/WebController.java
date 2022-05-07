package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exceptions.InvalidPageException;
import ar.edu.itba.paw.model.exceptions.UnauthorizedUserException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.CreateDebateForm;
import ar.edu.itba.paw.webapp.form.ModeratorForm;
import ar.edu.itba.paw.webapp.form.ProfileImageForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public WebController(UserService userService, DebateService debateService, PostService postService, ImageService imageService) {
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
            return registerPage(form);
        }
        userService.create(form.getUsername(), form.getPassword(), form.getEmail());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.GET, RequestMethod.HEAD})
    public ModelAndView profilePage(@ModelAttribute("profileImageForm") final ProfileImageForm form, Authentication auth, @RequestParam(value = "list", defaultValue = "subscribed") String list, @RequestParam(value = "page", defaultValue = "0") String page) {
        if (!page.matches("-?\\d+")) throw new InvalidPageException();

        final ModelAndView mav = new ModelAndView("/pages/profile");

        if (!list.equals("subscribed") && !list.equals("mydebates"))
            list = "subscribed";

        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
        mav.addObject("user", user);
        mav.addObject("total_pages", debateService.getProfileDebatesPageCount(list, user.getUserId()));
        mav.addObject("debates", debateService.getProfileDebates(list, user.getUserId(), Integer.parseInt(page)));
        return mav;
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.POST})
    public ModelAndView editProfileImage(@Valid @ModelAttribute("profileImageForm") final ProfileImageForm form, BindingResult errors, Authentication auth) throws IOException {
        if(errors.hasErrors()) {
            return profilePage(form, auth, "subscribed", "0");
        }
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        userService.updateImage(auth.getName(), form.getFile().getBytes());
        return new ModelAndView("redirect:/profile");
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public byte[] getImage(@PathVariable("imageId") final String imageId) {
        if (!imageId.matches("\\d+")) throw new ImageNotFoundException();

        return imageService.getImage(Integer.parseInt(imageId)).orElseThrow(ImageNotFoundException::new).getData();
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

        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedUserException();
        }

        debateService.create(form.getTitle(),
                form.getDescription(),
                auth.getName(),
                form.getOpponentUsername(),
                form.getImage().getBytes(),
                form.getCategory());
        return new ModelAndView("redirect:/debates");
    }

    @RequestMapping(value = "/404")
    public ModelAndView error() {
        return new ModelAndView("error/404");
    }
    // TODO: implement 403 error page
    @RequestMapping(value = "/403")
    public ModelAndView error403() {
        return new ModelAndView("error/404");
    }
}
