package ar.edu.itba.paw.model.exceptions;

public class DebateClosedException extends Exception409 {
    public DebateClosedException() {
        super("Voting has been disabled due to closed or deleted debate", "error.conflict.debate.closed");
    }
}
