package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.exceptions.StatusCodeException;

public class ExceptionErrorDto {

    private String message;
    private String messageCode;

    public static ExceptionErrorDto fromException(StatusCodeException exception) {
        final ExceptionErrorDto dto = new ExceptionErrorDto();

        dto.message = exception.getMessage();
        dto.messageCode = exception.getMessageCode();

        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }
}
