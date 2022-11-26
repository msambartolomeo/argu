package ar.edu.itba.paw.model.exceptions;

public class ForbiddenLikeException extends Exception403{
    public ForbiddenLikeException() {
        super("Trying to access likes of another user", "error.forbidden.like");
    }
}
