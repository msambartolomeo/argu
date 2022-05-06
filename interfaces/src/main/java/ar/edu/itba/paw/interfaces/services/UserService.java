package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);
    Optional<User> getUserByUsername(String username);
    User create(String username, String password, String email);

    Optional<User> getUserByEmail(String email);

    void updateImage(long id, byte[] image);

    void requestModerator(String username, String reason);
}
