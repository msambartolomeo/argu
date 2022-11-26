package ar.edu.itba.paw.model.exceptions;

public abstract class Exception400 extends StatusCodeException {
    public Exception400(String message, String messageCode) {
        super(message, 400, messageCode);
    }
}
