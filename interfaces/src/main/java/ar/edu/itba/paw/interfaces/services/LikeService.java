package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Argument;
import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface LikeService{
    Optional<Like> getLike(Argument argument, User user);
    Like likeArgument(long argumentId, String username);
    boolean unlikeArgument(long argumentId, String username);
    boolean isLiked(long argumentId, String username);
    boolean isLiked(User user, Argument argument);
}
