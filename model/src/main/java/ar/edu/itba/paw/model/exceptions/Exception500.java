package ar.edu.itba.paw.model.exceptions;

public class Exception500 extends StatusCodeException {
    public Exception500(String message, String messageCode) {
        super(message, 500, messageCode);
    }
}
