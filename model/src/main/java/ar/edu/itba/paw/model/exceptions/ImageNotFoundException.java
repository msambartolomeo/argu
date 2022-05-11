package ar.edu.itba.paw.model.exceptions;

public class ImageNotFoundException extends Exception404 {
    public ImageNotFoundException() {
        super("Image requested not found.");
    }
    @Override
    public String getMessageCode() {
        return "error.image.not.found";
    }
}
