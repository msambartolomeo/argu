package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotExistingUsernameValidator implements ConstraintValidator<NotExistingUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(NotExistingUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userService.getUserByUsername(value).isPresent();
    }
}
