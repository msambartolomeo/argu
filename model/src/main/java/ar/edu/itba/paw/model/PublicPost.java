package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.ArgumentStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PublicPost {
    private final long postId;
    private final String username;
    private final long debateId;
    private final String content;
    private final int likes;
    private final String createdDateString;
    private final Long imageId;
    private final ArgumentStatus status;

    public PublicPost(long postId, String username, long debateId, String content, int likes, LocalDateTime createdDate, Long imageId, ArgumentStatus status) {
        this.postId = postId;
        this.username = username;
        this.debateId = debateId;
        this.content = content;
        this.likes = likes;
        this.createdDateString = createdDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy"));
        this.imageId = imageId;
        this.status = status;
    }

    public long getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public long getDebateId() {
        return debateId;
    }

    public String getContent() {
        return content;
    }

    public int getLikes() {
        return likes;
    }

    public String getCreatedDate() {
        return createdDateString;
    }

    public Long getImageId() {
        return imageId;
    }

    public ArgumentStatus getStatus() {
        return status;
    }
}
