package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Post;

import java.util.Optional;

public interface PostService {

    Optional<Post> getPostById(int id);
    Post create(long userId, long debateId, String content);
    Post createWithEmail(String userEmail, long debateId, String content);
}
