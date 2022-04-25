package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    //TODO: Verificar que aquí sea el módulo correcto para esto
    private final boolean isEmail;
    private final boolean isUsername;

    public UserAlreadyExistsException(boolean isEmail, boolean isUsername) {
        super("User already exists");
        this.isEmail = isEmail;
        this.isUsername = isUsername;
    }

    public boolean isEmail() {
        return isEmail;
    }

    public boolean isUsername() {
        return isUsername;
    }
}