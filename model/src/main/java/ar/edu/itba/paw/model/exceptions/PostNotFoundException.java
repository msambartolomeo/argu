package ar.edu.itba.paw.model.exceptions;

public class PostNotFoundException extends Exception404 {
    @Override
    public String getMessageCode() {
        return "error.post.not.found";
    }
}
