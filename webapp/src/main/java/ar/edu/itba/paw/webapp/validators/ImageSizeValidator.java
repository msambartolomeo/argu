package ar.edu.itba.paw.webapp.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;

public class ImageSizeValidator implements ConstraintValidator<ImageSize, InputStream> {
    @Override
    public void initialize(ImageSize constraintAnnotation) {

    }

    @Override
    public boolean isValid(InputStream value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            return value.available() < 1024*1024*10;
        } catch (IOException e) {
            return false;
        }
    }
}
