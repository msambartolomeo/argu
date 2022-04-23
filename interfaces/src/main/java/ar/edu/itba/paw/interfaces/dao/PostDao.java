package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Optional<Post> getPostById(long id);
    List<Post> getPostsByDebate(long debateId, int page);
    Post create(long userId, long debateId, String content, Long imageId);
    Optional<PublicPost> getPublicPostById(long id);
    List<PublicPost> getPublicPostsByDebate(long debateId, int page);
    void likePost(long postId, long userId);
    void unlikePost(long postId, long userId);
}
