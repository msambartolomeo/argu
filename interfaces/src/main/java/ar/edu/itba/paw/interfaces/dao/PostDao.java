package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Optional<Post> getPostById(long postId);

    int getPostsByDebateCount(long debateId);

    List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, long userId, int page);
    Post create(User user, long debateId, String content, Image image, ArgumentStatus status);
    List<PublicPost> getPublicPostsByDebate(long debateId, int page);
    void likePost(long postId, long userId);
    void unlikePost(long postId, long userId);

    boolean hasLiked(long postId, long userId);
    Optional<PublicPost> getLastArgument(long debateId);
}
