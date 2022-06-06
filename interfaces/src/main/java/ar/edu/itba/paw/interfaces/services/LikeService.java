package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface LikeService{
    Optional<Like> getLike(Argument argument, User user);
    void likeArgument(long argumentId, String username);
    void unlikeArgument(long argumentId, String username);
    boolean isLiked(User user, Argument argument);
}
