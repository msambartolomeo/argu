package ar.edu.itba.paw.model.exceptions;

public class ArgumentDeletedException extends Exception409 {
    public ArgumentDeletedException() {
        super("The argument is already deleted.", "error.conflict.argument.deleted");
    }
}
