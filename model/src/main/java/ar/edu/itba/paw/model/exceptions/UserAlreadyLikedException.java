package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyLikedException extends Exception400 {
    @Override
    public String getMessageCode() {
        return "error.already-liked";
    }
}
