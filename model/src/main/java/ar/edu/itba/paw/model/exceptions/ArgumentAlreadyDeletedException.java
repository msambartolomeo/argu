package ar.edu.itba.paw.model.exceptions;

public class ArgumentAlreadyDeletedException extends Exception409 {
    public ArgumentAlreadyDeletedException() {
        super("The argument is already deleted.", "error.conflict.argument");
    }
}
