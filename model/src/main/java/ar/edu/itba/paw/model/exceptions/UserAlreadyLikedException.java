package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyLikedException extends Exception409 {
    public UserAlreadyLikedException() {
        super("User already liked the argument.", "error.conflict.likes");
    }
}
