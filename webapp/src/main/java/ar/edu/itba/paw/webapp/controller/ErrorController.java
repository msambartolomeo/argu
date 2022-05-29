package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
    @ExceptionHandler(Exception404.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView handleException404(Exception404 e) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("exception", e.getMessageCode());
        LOGGER.error("error 404 - {}", e.toString());
        return mav;
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ModelAndView handleExceptionUnauthorizedUser(UnauthorizedUserException e) {
        LOGGER.error("error 401 - {}", e.toString());
        return new ModelAndView("pages/login");
    }

    @ExceptionHandler(Exception403.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView handleForbiddenArgumentException(Exception403 e) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("exception", e.getMessageCode());
        LOGGER.error("error 403 - {}", e.toString());
        return mav;
    }

    @ExceptionHandler(Exception500.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException500(Exception500 e) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("exception", e.getMessageCode());
        LOGGER.error("error 500 - {}", e.toString());
        return mav;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        LOGGER.error("error 400", e);
        return new ModelAndView("error/400");
    }

    @ExceptionHandler(Exception400.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView handleException400(Exception400 e) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("exception", e.getMessageCode());
        LOGGER.error("error 400 - {}", e.toString());
        return mav;
    }
}
