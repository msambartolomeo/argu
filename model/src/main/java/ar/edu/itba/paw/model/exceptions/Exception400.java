package ar.edu.itba.paw.model.exceptions;

public abstract class Exception400 extends StatusCodeException {
    public Exception400(String message) {
        super(message, 400);
    }

    public String getMessageCode() {
        return "BadRequest";
    }
}
