package ar.edu.itba.paw.webapp.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageSizeValidator implements ConstraintValidator<ImageSize, MultipartFile> {
    @Override
    public void initialize(ImageSize constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;
        return value.getSize() < 1024*1024*10;
    }
}
