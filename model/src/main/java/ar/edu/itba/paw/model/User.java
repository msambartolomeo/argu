package ar.edu.itba.paw.model;

import java.time.LocalDate;

public class User {
    private final long userId;
    private final String username;
    private final String password;
    private final LocalDate createdDate;
    private final String email;

    public User(long userId, String username, String password, String email, LocalDate createdDate) {
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

    public LocalDate getcreatedDate() {
        return createdDate;
    }
}
