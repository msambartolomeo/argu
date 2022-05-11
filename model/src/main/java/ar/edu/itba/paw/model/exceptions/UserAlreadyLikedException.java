package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyLikedException extends Exception400 {
    public UserAlreadyLikedException() {
        super("User already liked the argument.");
    }
    @Override
    public String getMessageCode() {
        return "error.already-liked";
    }
}
