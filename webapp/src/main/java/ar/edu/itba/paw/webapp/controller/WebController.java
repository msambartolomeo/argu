package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.PostService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.webapp.exception.DebateNotFoundException;
import ar.edu.itba.paw.webapp.form.PostForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class WebController {

    private final UserService userService;
    private final DebateService debateService;
    private final PostService postService;

    @Autowired
    public WebController(UserService userService, DebateService debateService, PostService postService) {
        this.userService = userService;
        this.debateService = debateService;
        this.postService = postService;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debatesList() {
        final ModelAndView mav = new ModelAndView("debates-list");
        List<Debate> debates = debateService.getAll(0);
        if (debates.isEmpty())
            throw new DebateNotFoundException();
        mav.addObject("debates", debates);
        return mav;
    }

    @RequestMapping(value = "/debate/{debateId}", method = { RequestMethod.GET, RequestMethod.HEAD })
    public ModelAndView debate(@PathVariable("debateId") final long debateId, @ModelAttribute("postForm") final PostForm form) {
        final ModelAndView mav = new ModelAndView("debate");
        mav.addObject("debate", debateService.getDebateById(debateId)/*.orElseThrow(DebateNotFoundException::new)*/); // TODO uncomment later
//        mav.addObject("posts", postService.getPostByDebate(debateId)); // TODO get post by debate
        return mav;
    }

    @RequestMapping(value = "/debate/{debateId}", method = { RequestMethod.POST })
    public ModelAndView createPost(@PathVariable("debateId") final long debateId, @Valid @ModelAttribute("postForm") final PostForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return debate(debateId, form);
        }
        postService.createWithEmail(form.getEmail(), debateId, form.getContent());
        return new ModelAndView("redirect:/debate/" + debateId);
    }

    @ExceptionHandler(DebateNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleUserNotFoundException() {
        return new ModelAndView("error/404");
    }
}
