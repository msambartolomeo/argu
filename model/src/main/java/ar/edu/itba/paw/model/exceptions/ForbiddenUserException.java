package ar.edu.itba.paw.model.exceptions;

public class ForbiddenUserException extends Exception403 {
    public ForbiddenUserException() {
        super("Tried to modify another user", "error.forbidden.user");
    }
}
