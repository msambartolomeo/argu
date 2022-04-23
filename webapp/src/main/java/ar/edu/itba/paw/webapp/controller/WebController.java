package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.DebateNotFoundException;
import ar.edu.itba.paw.webapp.exception.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.PostForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
        return new ModelAndView("pages/landing-page");
    }

    @RequestMapping(value = "/debates", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debatesList() {
        final ModelAndView mav = new ModelAndView("pages/debates-list");
        mav.addObject("debates", debateService.getAll(0));
        return mav;
    }

    @RequestMapping(value = "/debates/{debateId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debate(@PathVariable("debateId") final long debateId, @ModelAttribute("postForm") final PostForm form) {
        final ModelAndView mav = new ModelAndView("pages/debate");
        mav.addObject("debate", debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new));
        mav.addObject("posts", postService.getPublicPostsByDebate(debateId, 0));
        return mav;
    }

    @RequestMapping(value = "/debates/{debateId}", method = { RequestMethod.POST })
    public ModelAndView createPost(@PathVariable("debateId") final long debateId, @Valid @ModelAttribute("postForm") final PostForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return debate(debateId, form);
        }
        postService.createWithEmail(form.getEmail(), debateId, form.getContent());
        return new ModelAndView("redirect:/debates/" + debateId);
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView loginPage() {
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
    public ModelAndView profilePage() {
        final ModelAndView mav = new ModelAndView("/pages/profile");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
        mav.addObject("user", user);
        mav.addObject("suscribed_debates", debateService.getSubscribedDebatesByUsername(user.getUserId(), 0));
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public byte[] getImage(@PathVariable("imageId") final long imageId) {
        Image image = imageService.getImage(imageId).orElseThrow(ImageNotFoundException::new);
        return image.getData();
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
}
