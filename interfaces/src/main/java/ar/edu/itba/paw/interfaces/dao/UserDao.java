package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User create(String username, String password, String email);
    void updateImage(long userId, long imageId);
    User updateLegacyUser(long userId, String username, String password, String email);
    List<User> getAll(int page); // TODO Deprecated
    List<User> getSubscribedUsersByDebate(long debateId);
}
