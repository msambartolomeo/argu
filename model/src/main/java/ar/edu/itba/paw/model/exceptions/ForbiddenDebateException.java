package ar.edu.itba.paw.model.exceptions;

public class ForbiddenDebateException extends Exception403{

    public ForbiddenDebateException() {
        super("Forbidden debate", "error.forbidden.debate");
    }
}
