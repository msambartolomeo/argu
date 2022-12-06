package ar.edu.itba.paw.model.exceptions;

// TODO: Check 400 vs 409
public class DebateAlreadyDeletedException extends Exception400{
    public DebateAlreadyDeletedException() {
        super("Voting has been disabled due to closed or deleted debate", "error.already-deleted.debate");
    }
}
