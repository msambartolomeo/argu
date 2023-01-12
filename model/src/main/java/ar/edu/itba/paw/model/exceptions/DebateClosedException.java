package ar.edu.itba.paw.model.exceptions;

public class DebateClosedException extends Exception409 {
    public DebateClosedException() {
        super("Debate is not open anymore so the operation can not be completed", "error.conflict.debate.closed");
    }
}
