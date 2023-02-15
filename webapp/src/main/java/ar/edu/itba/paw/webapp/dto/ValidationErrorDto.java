package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;

public class ValidationErrorDto {

    private String message;
    private String path;
    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> violation) {
        final ValidationErrorDto dto = new ValidationErrorDto();
        dto.message = violation.getMessage();
        dto.path = violation.getPropertyPath().toString();
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
