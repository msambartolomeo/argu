package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.exceptions.UserConflictException;

public class UserConflictExceptionDto {

    private String message;
    private String messageCode;
    private boolean username;
    private boolean email;


    public static UserConflictExceptionDto fromException(UserConflictException exception) {
        final UserConflictExceptionDto dto = new UserConflictExceptionDto();

        dto.message = exception.getMessage();
        dto.messageCode = exception.getMessageCode();
        dto.username = exception.isUsername();
        dto.email = exception.isEmail();

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

    public boolean isUsername() {
        return username;
    }

    public void setUsername(boolean username) {
        this.username = username;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }
}
