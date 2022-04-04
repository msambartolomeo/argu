package ar.edu.itba.paw.model;

public class User {
    private final long id;
    private final String username;
    private final String password;

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }
}
