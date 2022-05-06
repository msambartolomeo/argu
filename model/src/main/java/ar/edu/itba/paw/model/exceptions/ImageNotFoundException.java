package ar.edu.itba.paw.model.exceptions;

public class ImageNotFoundException extends Exception404 {
    @Override
    public String getMessageCode() {
        return "error.image.not.found";
    }
}
