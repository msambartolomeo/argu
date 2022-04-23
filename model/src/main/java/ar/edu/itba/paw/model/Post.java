package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class Post {
    private final long postId;
    private final long userId;
    private final long debateId;
    private final String content;
    private final LocalDateTime creationDate;
    private final Long imageId;

    public Post(long postId, long userId, long debateId, String content, LocalDateTime creationDate, Long imageId) {
        this.postId = postId;
        this.userId = userId;
        this.debateId = debateId;
        this.content = content;
        this.creationDate = creationDate;
        this.imageId = imageId;
    }

    public Post(long postId, long userId, long debateId, String content, LocalDateTime creationDate) {
        this(postId, userId, debateId, content, creationDate, null);
    }

    public long getPostId() {
        return postId;
    }

    public long getUserId() {
        return userId;
    }

    public long getDebateId() {
        return debateId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getImageId() {
        return imageId;
    }
}
