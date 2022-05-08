package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;
import ar.edu.itba.paw.model.PublicPostWithUserLike;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> getPostById(long id);
    Post create(String username, long debateId, String content, byte[] image);
    List<Post> getPostsByDebate(long debateId, int page);
    int getPostsByDebateCount(long debateId);
    Optional<PublicPost> getPublicPostById(long id);
    List<PublicPost> getPublicPostsByDebate(long debateId, int page);
    void likePost(long postId, String username);
    void unlikePost(long postId, String username);

    boolean hasLiked(long postId, String username);

    List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, String username, int page);

    Optional<PublicPost> getLastArgument(long debateIdNum);
}
