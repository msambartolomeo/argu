package ar.edu.itba.paw.model.exceptions;

public class ArgumentTurnException extends Exception409 {
    public ArgumentTurnException() {
        super("It is not the turn of the requesting user to post an argument", "error.conflict.argument.turn");
    }
}
