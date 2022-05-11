package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyVotedException extends Exception400 {
    public UserAlreadyVotedException() {
        super("User already voted in this debate.");
    }
    @Override
    public String getMessageCode() {
        return "error.already-voted";
    }
}
