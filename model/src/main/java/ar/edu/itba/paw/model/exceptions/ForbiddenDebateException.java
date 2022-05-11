package ar.edu.itba.paw.model.exceptions;

public class ForbiddenDebateException extends Exception403{

    @Override
    public String getMessageCode() {
        return "error.debate.not.found";
    }
}
