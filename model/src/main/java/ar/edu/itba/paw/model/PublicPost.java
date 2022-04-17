package ar.edu.itba.paw.model;

public class PublicPost {
    private final long postId;
    private final String userEmail;
    private final long debateId;
    private final String content;
    private final int likes;

    public PublicPost(long postId, String userEmail, long debateId, String content, int likes) {
        this.postId = postId;
        this.userEmail = userEmail;
        this.debateId = debateId;
        this.content = content;
        this.likes = likes;
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

    public int getLikes() {
        return likes;
    }
}
