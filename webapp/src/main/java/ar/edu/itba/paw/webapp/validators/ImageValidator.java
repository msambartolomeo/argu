package ar.edu.itba.paw.webapp.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {
    @Override
    public void initialize(Image constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value.isEmpty()) return true;
        return value.getContentType().startsWith("image/");
    }
}
