package ar.edu.itba.paw.model.exceptions;

public class DebateNotFoundException extends Exception404 {
    public DebateNotFoundException() {
        super("Debate requested not found.", "error.not-found.debate");
    }
}
