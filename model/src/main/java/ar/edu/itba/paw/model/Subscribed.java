package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.keys.UserDebateKey;

import javax.persistence.*;

@Entity
@Table(name = "subscribed2")
public class Subscribed {

    @EmbeddedId
    private UserDebateKey userDebateKey;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @MapsId("debateId")
    @JoinColumn(name = "debateid")
    private Debate debate;

    Subscribed() {}

    public Subscribed(User user, Debate debate) {
        this.user = user;
        this.debate = debate;
        this.userDebateKey = new UserDebateKey(user.getUserId(), debate.getDebateId());
    }
}
