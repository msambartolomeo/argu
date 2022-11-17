package ar.edu.itba.paw.webapp.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;

public class ImageExistanceValidator implements ConstraintValidator<ImageExistance, InputStream> {
    @Override
    public void initialize(ImageExistance constraintAnnotation) {

    }

    @Override
    public boolean isValid(InputStream value, ConstraintValidatorContext context) {
        try {
            return value != null && value.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }
}