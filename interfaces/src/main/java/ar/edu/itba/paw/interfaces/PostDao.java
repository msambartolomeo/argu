package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Optional<Post> getPostById(long id);
    List<Post> getAll(int page, int pageSize);
    Post create(long userId, long debateId, String content);
}
