package ar.edu.itba.paw.model.exceptions;

public abstract class Exception403 extends StatusCodeException {
    public Exception403(String message) {
        super(message, 403);
    }

    public String getMessageCode() {
        return null;
    }

}
