package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NotExistingEmailValidator implements ConstraintValidator<NotExistingEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(NotExistingEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> user = userService.getUserByEmail(value);
        return !user.isPresent() || user.get().getUsername() == null;
    }
}
