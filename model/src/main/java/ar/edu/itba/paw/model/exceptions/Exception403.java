package ar.edu.itba.paw.model.exceptions;

public abstract class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public String getMessageCode() {
        return null;
    }

}
