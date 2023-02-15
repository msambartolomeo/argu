package ar.edu.itba.paw.model.exceptions;

public class SubscriptionNotFoundException extends Exception404 {

    public SubscriptionNotFoundException() {
        super("Subscription not found.", "error.not-found.subscription");
    }
}
