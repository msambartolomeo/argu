package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.UserRole;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User {
    private final long userId;
    private final String username;
    private final String password;
    private final String createdDate;
    private final String email;
    private final Long imageId;
    private final UserRole role;

    public User(long userId, String username, String password, String email, LocalDate createdDate, Long imageId, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.email = email;
        this.imageId = imageId;
        this.role = role;
    }

    public User(long userId, String username, String password, String email, LocalDate createdDate, UserRole role) {
        this(userId, username, password, email, createdDate, null, role);
    }

    public String getEmail() {
        return email;
    }

    public Long getImageId() {
        return imageId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public long getUserId() {
        return userId;
    }

    public UserRole getRole() {
    	return role;
    }
}
