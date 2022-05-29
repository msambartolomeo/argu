package ar.edu.itba.paw.model.exceptions;

public class ArgumentNotFoundException extends Exception404 {
    public ArgumentNotFoundException() {
        super("Requested argument not found");
    }
    @Override
    public String getMessageCode() {
        return "error.post.not.found";
    }
}
