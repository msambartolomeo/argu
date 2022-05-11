package ar.edu.itba.paw.model.exceptions;

public class DebateNotFoundException extends Exception404 {
    public DebateNotFoundException() {
        super("Debate requested not found.");
    }
    @Override
    public String getMessageCode() {
        return "error.debate.not.found";
    }
}
