package ar.edu.itba.paw.model.exceptions;

public class VoteNotFoundException extends Exception404 {

    public VoteNotFoundException() {
        super("Vote not found.", "error.not-found.vote");
    }
}
