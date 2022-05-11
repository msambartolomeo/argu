package ar.edu.itba.paw.model.enums;

public enum UserRole {
    USER,
    MODERATOR;

    public static UserRole getRole(Integer role) {
        return UserRole.values()[role];
    }
}
