package ar.edu.itba.paw.model.exceptions;

public class UserAlreadySubscribedException extends Exception409 {
    public UserAlreadySubscribedException() {
        super("User already subscribed to the debate", "error.conflict.subscriptions");
    }
}
