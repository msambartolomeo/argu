package ar.edu.itba.paw.model.exceptions;

public class DebateNotFoundException extends Exception404 {
    @Override
    public String getMessageCode() {
        return "error.debate.not.found";
    }
}
