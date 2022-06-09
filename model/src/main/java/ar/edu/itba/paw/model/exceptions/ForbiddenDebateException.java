package ar.edu.itba.paw.model.exceptions;

public class ForbiddenDebateException extends Exception403{

    public ForbiddenDebateException() {
        super("Forbidden debate");
    }
    @Override
    public String getMessageCode() {
        return "error.forbidden.debate";
    }
}
