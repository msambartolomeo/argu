package ar.edu.itba.paw.webapp.validators;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidator implements ConstraintValidator<Image, FormDataBodyPart> {
    @Override
    public void initialize(Image constraintAnnotation) {

    }

    @Override
    public boolean isValid(FormDataBodyPart value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.getMediaType().toString().startsWith("image/");
    }
}
