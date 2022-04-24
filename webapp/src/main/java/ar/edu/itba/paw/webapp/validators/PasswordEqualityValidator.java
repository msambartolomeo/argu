package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.form.RegisterForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, RegisterForm> {
    @Override
    public void initialize(PasswordEquality constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegisterForm form, ConstraintValidatorContext context) {
        String password = form.getPassword();
        String passwordConfirmation = form.getPasswordConfirmation();

        return password.equals(passwordConfirmation);
    }
}
