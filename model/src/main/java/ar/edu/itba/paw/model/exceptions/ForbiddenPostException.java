package ar.edu.itba.paw.model.exceptions;

public class ForbiddenPostException extends Exception403 {

    @Override
    public String getMessageCode() {
        return "error.post.not.found";
    }
}
