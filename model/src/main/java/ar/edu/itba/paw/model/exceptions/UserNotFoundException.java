package ar.edu.itba.paw.model.exceptions;

public class UserNotFoundException extends Exception404 {
    public UserNotFoundException() {
        super("User requested not found.");
    }
    @Override
    public String getMessageCode() {
        return "error.user.not.found";
    }
}
