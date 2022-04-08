package ar.edu.itba.paw.model;

public class User {
    private final long userId;
    private final String email;

    public User(long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return userId;
    }
}
