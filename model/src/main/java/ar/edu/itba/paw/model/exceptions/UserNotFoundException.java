package ar.edu.itba.paw.model.exceptions;

public class UserNotFoundException extends Exception404 {
    @Override
    public String getMessageCode() {
        return "error.user.not.found";
    }
}
