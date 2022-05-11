package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyVotedException extends Exception400 {
    @Override
    public String getMessageCode() {
        return "error.already-voted";
    }
}
