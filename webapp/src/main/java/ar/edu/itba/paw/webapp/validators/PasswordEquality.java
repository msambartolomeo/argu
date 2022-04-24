package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordEqualityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordEquality {
    String message() default "The passwords don't match";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}