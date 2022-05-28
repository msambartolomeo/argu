package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Like;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface LikeService{

    Optional<Like> getLike(Post post, User user);

    void likePost(long postId, String username);

    void unlikePost(long postId, String username);
}
