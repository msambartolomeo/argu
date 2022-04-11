package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Optional<Post> getPostById(long id);
    List<Post> getPostsByDebate(long debateId, int page);
    Post create(long userId, long debateId, String content);
    // Without pagination, to be used internally
    List<Post> getAllPostByDebate(long debateId);
}
