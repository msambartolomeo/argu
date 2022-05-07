package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.ArgumentStatus;

import java.time.LocalDateTime;

public class PublicPostWithUserLike extends PublicPost {
    private final Boolean isLiked;

    public PublicPostWithUserLike(long postId, String username, long debateId, String content, int likes, LocalDateTime createdDate, Long imageId, ArgumentStatus status, Boolean isLiked) {
        super(postId, username, debateId, content, likes, createdDate, imageId, status);
        this.isLiked = isLiked;
    }

    public Boolean getLiked() {
        return isLiked;
    }
}
