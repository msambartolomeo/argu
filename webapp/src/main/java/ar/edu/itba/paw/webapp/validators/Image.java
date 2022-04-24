package ar.edu.itba.paw.webapp.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Image {
    String message() default "The uploaded file is not an image";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
