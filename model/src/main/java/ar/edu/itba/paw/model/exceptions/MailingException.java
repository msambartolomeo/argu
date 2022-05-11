package ar.edu.itba.paw.model.exceptions;

public class MailingException extends RuntimeException{
    public MailingException(String message, Throwable cause) {
        super(message, cause);
    }
}
