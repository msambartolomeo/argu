package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.PostForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class HelloWorldController {

    private final UserService userService;

    @Autowired
    public HelloWorldController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/debate", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debate(@ModelAttribute("postForm") final PostForm form) {
        return new ModelAndView("debate");
    }

    @RequestMapping(value = "/debate", method = { RequestMethod.POST })
    public ModelAndView createPost(@Valid @ModelAttribute("postForm") final PostForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return debate(form);
        }
        // TODO user service create post
        return new ModelAndView("redirect:/debate");
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView helloWorld(@RequestParam(name = "userId", defaultValue = "1") final long userId) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/profile/{userId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", userService.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam(value = "name") final String username) {
            final User user = userService.create(username);
            return new ModelAndView("redirect:/?userId=" + user.getId());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleUserNotFoundException() {
        return new ModelAndView("error/404");
    }

}
