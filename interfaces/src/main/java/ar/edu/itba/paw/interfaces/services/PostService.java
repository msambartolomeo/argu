package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post create(String username, long debateId, String content, byte[] image);
    int getPostsByDebatePageCount(long debateId);
    Optional<Post> getPostById(long postId);
    List<Post> getPostsByDebate(long debateId, String username, int page);
    Optional<Post> getLastArgument(long debateId);
}
