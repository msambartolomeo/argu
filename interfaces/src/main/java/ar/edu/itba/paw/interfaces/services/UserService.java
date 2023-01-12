package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;

import java.util.Locale;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUsername(String username);
    User create(String username, String password, String email, Locale locale);
    Optional<User> getUserByEmail(String email);
    User updateImage(String username, byte[] image);
    boolean deleteImage(String username);
    void requestModerator(String username, String reason);
    void deleteUser(String username);

    Optional<Image> getUserImage(String username);
}
