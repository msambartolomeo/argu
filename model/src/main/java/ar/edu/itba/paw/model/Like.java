package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.keys.UserPostKey;

import javax.persistence.*;

@Entity
@Table(name = "likes")
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
    private Argument argument;

    public Like() {}

    public Like(User user, Argument argument) {
        this.user = user;
        this.argument = argument;
        this.userPostKey = new UserPostKey(user.getUserId(), argument.getArgumentId());
    }

    public UserPostKey getUserPostKey() {
        return userPostKey;
    }

    public User getUser() {
        return user;
    }

    public Argument getArgument() {
        return argument;
    }
}
