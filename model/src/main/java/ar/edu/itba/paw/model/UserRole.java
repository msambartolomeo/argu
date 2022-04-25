package ar.edu.itba.paw.model;

public enum UserRole {
    USER,
    MODERATOR;

    public static UserRole getRole(Integer role) {
        return UserRole.values()[role];
    }
    public static int getValue(UserRole role) {
        return role.ordinal();
    }
}
