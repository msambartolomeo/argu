package ar.edu.itba.paw.model.exceptions;

public class PostNotFoundException extends Exception404 {
    public PostNotFoundException() {
        super("Requested post not found");
    }
    @Override
    public String getMessageCode() {
        return "error.post.not.found";
    }
}
