package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUsername(String username);

    User create(String username, String password, String email);

    Optional<User> getUserByEmail(String email);

    void updateImage(String username, byte[] image);

    void requestModerator(String username, String reason);

    // TODO: moved to Debate model (not implemented yet) remove for migration merge
    List<User> getSubscribedUsersByDebate(long debateId);
}
