package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@RegisterForm.PasswordEquality
public class RegisterForm {

    @Size(max = 64)
    @NotEmpty
    private String username;

    @Size(max = 100)
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(max = 100)
    private String password;

    @NotEmpty
    @Size(max = 100)
    private String passwordConfirmation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Constraint(validatedBy = PasswordEqualityValidator.class)
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PasswordEquality {
        String message() default "The passwords don't match";
        Class<?>[] groups() default { };
        Class<? extends Payload>[] payload() default { };
    }

    public static class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, RegisterForm> {
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
}


