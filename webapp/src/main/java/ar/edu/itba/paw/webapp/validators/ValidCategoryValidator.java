package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.model.enums.DebateCategory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidCategoryValidator implements ConstraintValidator<ValidCategory, String> {

    @Override
    public void initialize(ValidCategory constraintAnnotation) {

    }

    @Override
    public boolean isValid(String category, ConstraintValidatorContext context) {
        return category == null || Arrays.stream(DebateCategory.values()).anyMatch((c) -> c.getName().equals(category));
    }
}
