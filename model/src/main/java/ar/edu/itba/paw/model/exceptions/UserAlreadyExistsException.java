package ar.edu.itba.paw.model.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    //TODO: Verificar que aquí sea el módulo correcto para esto
    private final boolean isEmail;
    private final boolean isUsername;

    private final String username;
    private final String email;
    private final String password;

    public UserAlreadyExistsException(boolean isEmail, boolean isUsername, String username, String email, String password) {
        super("User already exists");
        this.isEmail = isEmail;
        this.isUsername = isUsername;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public boolean isEmail() {
        return isEmail;
    }

    public boolean isUsername() {
        return isUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}