package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Optional<Post> getPostById(long postId);
    int getPostsByDebateCount(long debateId);
    Post create(User user, Debate debate, String content, Image image, ArgumentStatus status);
    List<Post> getPostsByDebate(Debate debate, int page);
    Optional<Post> getLastArgument(Debate debate);
}
