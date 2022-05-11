package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingUserValidator implements ConstraintValidator<ExistingUser, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistingUser constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return userService.getUserByUsername(username).isPresent();
    }
}
