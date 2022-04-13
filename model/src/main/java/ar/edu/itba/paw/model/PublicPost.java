package ar.edu.itba.paw.model;

public class PublicPost {
    private final long postId;
    private final String userEmail;
    private final long debateId;
    private final String content;

    public PublicPost(long postId, String userEmail, long debateId, String content) {
        this.postId = postId;
        this.userEmail = userEmail;
        this.debateId = debateId;
        this.content = content;
    }

    public long getPostId() {
        return postId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public long getDebateId() {
        return debateId;
    }

    public String getContent() {
        return content;
    }
}
