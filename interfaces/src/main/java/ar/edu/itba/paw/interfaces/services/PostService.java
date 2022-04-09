package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> getPostById(int id);
    Post create(long userId, long debateId, String content);
    Post createWithEmail(String userEmail, long debateId, String content);
    List<Post> getPostsByDebate(long debateId, int page);
}
