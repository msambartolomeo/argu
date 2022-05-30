package ar.edu.itba.paw.model.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPostKey implements Serializable {

    @Column(name = "userid")
    private Long userId;

    @Column(name = "postid")
    private Long postId;

    /*default*/UserPostKey() {
        // Just for Hibernate
    }

    public UserPostKey(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPostKey that = (UserPostKey) o;
        return userId.equals(that.userId) && postId.equals(that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }

}
