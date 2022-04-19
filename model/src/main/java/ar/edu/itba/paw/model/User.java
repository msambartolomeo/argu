package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class User {
    private final long userId;
    private final String username;
    private final String password;
    private final LocalDateTime createdDate;
    private final String email;

    public User(long userId, String username, String password, String email, LocalDateTime createdDate) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getcreatedDate() {
        return createdDate;
    }
}
