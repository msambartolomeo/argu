package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class PublicPost {
    private final long postId;
    private final String username;
    private final long debateId;
    private final String content;
    private final int likes;
    private final LocalDateTime createdDate;
    private final Long imageId;

    public PublicPost(long postId, String username, long debateId, String content, int likes, LocalDateTime createdDate, Long imageId) {
        this.postId = postId;
        this.username = username;
        this.debateId = debateId;
        this.content = content;
        this.likes = likes;
        this.createdDate = createdDate;
        this.imageId = imageId;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Long getImageId() {
        return imageId;
    }
}
