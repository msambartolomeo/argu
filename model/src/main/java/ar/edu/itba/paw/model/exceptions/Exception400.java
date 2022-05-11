package ar.edu.itba.paw.model.exceptions;

public abstract class Exception400 extends RuntimeException{
    public Exception400(String message) {
        super(message);
    }

    public String getMessageCode() {
        return "BadRequest";
    }
}
