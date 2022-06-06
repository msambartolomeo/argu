package ar.edu.itba.paw.model.exceptions;

public class ForbiddenArgumentException extends Exception403 {
    public ForbiddenArgumentException() {
        super("Forbidden argument");
    }

    @Override
    public String getMessageCode() {
        return "error.forbidden.argument";
    }
}
