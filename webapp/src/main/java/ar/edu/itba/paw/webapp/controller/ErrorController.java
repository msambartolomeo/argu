package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception404.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleException404(Exception404 e) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("exception", e.getMessageCode());
        return mav;
    }

    @ExceptionHandler(ForbiddenPostException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView handleForbiddenPostException(ForbiddenPostException e) {
        return new ModelAndView("error/404");
        // TODO: change to 403
    }
}
