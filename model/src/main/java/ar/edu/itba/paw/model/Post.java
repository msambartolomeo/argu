package ar.edu.itba.paw.model;

public class Post {
    private final long postId;
    private final long userId;
    private final long debateId;
    private final String content;
    
    public Post(long postId, long userId, long debateId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.debateId = debateId;
        this.content = content;
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
}
