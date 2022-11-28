package ar.edu.itba.paw.model.exceptions;

public class ForbiddenVoteException extends Exception403 {
    public ForbiddenVoteException() {
        super("Trying to access votes of another user", "error.forbidden.vote");
    }
}
