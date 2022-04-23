package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);

    User create(String username, String password, String email, Long imageId);

    // int pageSize
    List<User> getAll(int page);

    List<User> getSuscribedUsersByDebate(long debateId);
}
