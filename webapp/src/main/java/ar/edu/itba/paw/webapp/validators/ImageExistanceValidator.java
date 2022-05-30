package ar.edu.itba.paw.webapp.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageExistanceValidator implements ConstraintValidator<ImageExistance, MultipartFile> {
    @Override
    public void initialize(ImageExistance constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value != null && !value.isEmpty();
    }
}