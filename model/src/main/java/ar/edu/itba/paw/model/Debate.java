package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "debates2") // TODO: Change to debates
public class Debate {

    @Id
    @GeneratedValue(generator = "debates_debateid_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "debates_debateid_seq", sequenceName = "debates_debateid_seq", allocationSize = 1)
    private Long debateId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "creatorid", nullable = false)
    private Long creatorId;
    @Column(name = "opponentid", nullable = false)
    private Long opponentId;
    @Column(name = "imageid")
    private Long imageId;

//    TODO: Uncomment when available
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    private User creator;
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    private User opponent;
//
//
//    @OneToOne(fetch = FetchType.EAGER)
//    private Image image;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private DebateCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DebateStatus status;


//    @Formula("(select count(*) from subscribed where debateid = debateid)")
//    private int subcribedUsers;
//
//    @Formula("(select count(*) from votes where debateid = debateid and vote = FOR)")
//    private int forCount;
//    @Formula("(select count(*) from votes where debateid = debateid and vote = AGAINST)")
//    private int againstCount;

    Debate() {}

    // TODO: Change to use User and Image instead of IDs
    public Debate(String name, String description, Long creatorId, Long opponentId, Long imageId, DebateCategory category, DebateStatus status) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.opponentId = opponentId;
        this.imageId = imageId;
        this.category = category;
        this.status = status;
        this.createdDate = LocalDateTime.now();
    }

    @Deprecated
    public Debate(long id, String name, String description, Long creatorId, Long opponentId, LocalDateTime createdDate, Long imageId, DebateCategory category, DebateStatus debateStatus) {}

    @Deprecated
    public Debate(long id, String name, String description, Long creatorId, Long opponentId, LocalDateTime createdDate, DebateCategory category, DebateStatus debateStatus) {}

    public long getDebateId() {
        return debateId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getOpponentId() {
        return opponentId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Long getImageId() {
        return imageId;
    }

    public DebateCategory getCategory() {
        return category;
    }

    public DebateStatus getStatus() {
        return status;
    }
}
