package ar.edu.itba.paw.model.exceptions;

public class ForbiddenArgumentException extends Exception403 {
    public ForbiddenArgumentException() {
        super("Forbidden argument", "error.forbidden.argument");
    }
}
