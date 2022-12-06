package ar.edu.itba.paw.model.exceptions;

public class ForbiddenSubscriptionException extends Exception403 {
    public ForbiddenSubscriptionException() {
        super("Trying to access subscriptions of another user", "error.forbidden.subscription");
    }
}
