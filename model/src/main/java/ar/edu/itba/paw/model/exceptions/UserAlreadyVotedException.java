package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyVotedException extends Exception409 {
    public UserAlreadyVotedException() {
        super("User already voted in this debate.", "error.conflict.votes");
    }
}
