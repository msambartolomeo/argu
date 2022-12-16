package ar.edu.itba.paw.model.exceptions;

public class ForbiddenArgumentException extends Exception403 {
    public ForbiddenArgumentException() {
        super("User does not have permissions to modify argument", "error.forbidden.argument");
    }
}
