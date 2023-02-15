package ar.edu.itba.paw.model.exceptions;

public class Exception409 extends StatusCodeException {
    public Exception409(String message, String messageCode) {
        super(message, 409, messageCode);
    }
}
