package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.User;

import java.util.Optional;
import java.util.Set;

public interface LikeDao {
    Optional<Like> getLike(User user, Post post);
    void likePost(User user, Post post);
    void unlikePost(User user, Post post);
}
