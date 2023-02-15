package ar.edu.itba.paw.model.exceptions;

public abstract class Exception404 extends StatusCodeException {

    public Exception404(String message, String messageCode) {
        super(message, 404, messageCode);
    }
}
