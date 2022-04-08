package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Optional<Post> getPostById(long id);
    List<Post> getAllByDebate(int page, long debateId);
    Post create(long userId, long debateId, String content);
}
