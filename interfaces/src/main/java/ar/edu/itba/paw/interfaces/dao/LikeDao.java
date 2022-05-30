package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface LikeDao {
    Optional<Like> getLike(User user, Argument argument);
    void likeArgument(User user, Argument argument);
    void unlikeArgument(User user, Argument argument);
}
