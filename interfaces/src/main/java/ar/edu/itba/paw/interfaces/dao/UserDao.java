package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    Optional<User> getUserByEmail(String email);

    User create(String email);

    // int pageSize
    List<User> getAll(int page);

    List<User> getSuscribedUsersByDebate(long debateId);
}
