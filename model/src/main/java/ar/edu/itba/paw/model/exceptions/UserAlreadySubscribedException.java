package ar.edu.itba.paw.model.exceptions;

public class UserAlreadySubscribedException extends Exception400{
    public UserAlreadySubscribedException() {
        super("User already subscribed to the debate", "error.already-subscribed");
    }
}
