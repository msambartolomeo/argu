package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VerifyPasswordValidator implements ConstraintValidator<VerifyPassword, String> {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(VerifyPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (value == null) return false;
        User toDelete = userService.getUserByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
        return passwordEncoder.matches(value, toDelete.getPassword());
    }
}
