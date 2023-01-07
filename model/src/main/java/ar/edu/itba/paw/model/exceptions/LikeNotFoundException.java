package ar.edu.itba.paw.model.exceptions;

public class LikeNotFoundException extends Exception404 {

    public LikeNotFoundException() {
        super("Like not found.", "error.not-found.like");
    }
}
