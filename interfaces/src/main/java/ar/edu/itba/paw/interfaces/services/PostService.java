package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> getPostById(long id);
    Post create(long userId, long debateId, String content);
    Post create(long userId, long debateId, String content, byte[] image);
    // TODO: Deprecated
    Post createWithEmail(String userEmail, long debateId, String content);
    List<Post> getPostsByDebate(long debateId, int page);
    Optional<PublicPost> getPublicPostById(long id);
    List<PublicPost> getPublicPostsByDebate(long debateId, int page);
    void likePost(long postId, long userId);
    void unlikePost(long postId, long userId);
}
