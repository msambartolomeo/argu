package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ModelAndView handleExceptionUnauthorizedUser() {
        return new ModelAndView("pages/login");
    }

    @ExceptionHandler(ForbiddenPostException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView handleForbiddenPostException(ForbiddenPostException e) {
        return new ModelAndView("error/404");
        // TODO: change to 403
    }

    @ExceptionHandler(Exception500.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException500(Exception500 e) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("exception", e.getMessageCode());
        return mav;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodNotAllowed() {
        return new ModelAndView("error/400");
    }

    @ExceptionHandler(Exception400.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handleException400(Exception400 e) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("exception", e.getMessageCode());
        return mav;
    }
}
