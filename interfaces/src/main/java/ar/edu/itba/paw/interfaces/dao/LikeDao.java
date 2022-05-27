package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.User;

import java.util.Set;

public interface LikeDao {
    Set<Long> getUserPostsLikesInDebate(long userid, long debateid);

    void likePost(User user, Post post);

    void unlikePost(User user, Post post);
}
