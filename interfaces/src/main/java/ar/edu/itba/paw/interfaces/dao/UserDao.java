package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User create(String username, String password, String email);
}
