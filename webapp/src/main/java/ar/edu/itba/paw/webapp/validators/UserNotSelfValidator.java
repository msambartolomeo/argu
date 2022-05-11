package ar.edu.itba.paw.webapp.validators;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNotSelfValidator implements ConstraintValidator<UserNotSelf, String> {

    @Override
    public void initialize(UserNotSelf constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() == null) return true;
        return !value.equals(auth.getName());
    }
}
