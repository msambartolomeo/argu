package ar.edu.itba.paw.model.exceptions;

public class ImageNotFoundException extends Exception404 {
    public ImageNotFoundException() {
        super("Image not found.", "error.not-found.image");
    }
}
