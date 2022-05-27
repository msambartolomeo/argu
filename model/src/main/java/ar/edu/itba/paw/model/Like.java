package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.keys.UserPostKey;

import javax.persistence.*;

@Entity
@Table(name = "likes2")
public class Like {

    @EmbeddedId
    private UserPostKey userPostKey;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "postid")
    private Post post;

    /*default*/public Like() {
        // Just for Hibernate
    }

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
        this.userPostKey = new UserPostKey(user.getUserId(), post.getPostId());
    }
}
