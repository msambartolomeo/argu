package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.keys.UserDebateKey;

import javax.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
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

    @Enumerated(EnumType.STRING)
    @Column(name = "vote", nullable = false, length = 10)
    private DebateVote vote;


    Vote() {}

    public Vote(User user, Debate debate) {
        this.user = user;
        this.debate = debate;
        this.userDebateKey = new UserDebateKey(user.getUserId(), debate.getDebateId());
    }
}
