package ar.edu.itba.paw.model.exceptions;

public class EntityTooLargeImageException extends StatusCodeException {
    public EntityTooLargeImageException() {
        super("The provided image is too big", 413, "error.entity-too-large.image");
    }
}
