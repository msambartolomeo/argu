package ar.edu.itba.paw.model.exceptions;

public class ArgumentAlreadyDeletedException extends Exception400{
    public ArgumentAlreadyDeletedException() {
        super("The argument is already deleted.", "error.already-deleted.argument");
    }
}
