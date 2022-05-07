package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);

    // TODO: Change method names when merged
    Optional<User> getUserByUsername(String username);
    User getRealUserByUsername(String username);


    User create(String username, String password, String email);

    Optional<User> getUserByEmail(String email);

    void updateImage(String username, byte[] image);

    void requestModerator(String username, String reason);

    List<User> getSubscribedUsersByDebate(long debateId);
}
