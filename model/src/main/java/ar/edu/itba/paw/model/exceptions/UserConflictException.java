package ar.edu.itba.paw.model.exceptions;

public class UserConflictException extends RuntimeException {

    private final boolean username;
    private final boolean email;

    public UserConflictException(boolean username, boolean email) {
        super("Username or email already exists");
        this.email = email;
        this.username = username;
    }

    public String getMessageCode() {
        return "error.conflict.user";
    }

    public boolean isUsername() {
        return username;
    }

    public boolean isEmail() {
        return email;
    }
}