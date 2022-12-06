package ar.edu.itba.paw.model.exceptions;

public class DebateAlreadyDeletedException extends Exception409 {
    public DebateAlreadyDeletedException() {
        super("Voting has been disabled due to closed or deleted debate", "error.conflict.debate");
    }
}
