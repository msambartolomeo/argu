package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Table(name = "debates")
public class Debate {

    @Id
    @GeneratedValue(generator = "debates_debateid_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "debates_debateid_seq", sequenceName = "debates_debateid_seq", allocationSize = 1)
    private Long debateId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creatorid")
    private User creator;

    @Column(name = "iscreatorfor", nullable = false, columnDefinition = "boolean default 'true'")
    private boolean isCreatorFor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "opponentid")
    private User opponent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageid")
    private Image image;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category", nullable = false, length = 20)
    private DebateCategory category;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false, length = 20)
    private DebateStatus status;

    public void setStatus(DebateStatus status) {
        this.status = status;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscribed",
            joinColumns = @JoinColumn(name = "debateid", referencedColumnName = "debateid"),
            inverseJoinColumns = @JoinColumn(name = "userid", referencedColumnName = "userid"))
    private Set<User> subscribedUsers;

    @Formula("(select count(*) from subscribed where subscribed.debateid = debateid)")
    private int subscribedUsersCount;

    @Formula("(select count(*) from votes where votes.debateid = debateid and votes.vote = 0)")
    private int forCount;
    @Formula("(select count(*) from votes where votes.debateid = debateid and votes.vote = 1)")
    private int againstCount;

    Debate() {}

    public Debate(String name, String description, User creator, boolean isCreatorFor, User opponent, Image image, DebateCategory category) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.isCreatorFor = isCreatorFor;
        this.opponent = opponent;
        this.image = image;
        this.category = category;
        this.status = DebateStatus.OPEN;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getFormattedDate() {
        return createdDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
    }

    public DebateCategory getCategory() {
        return category;
    }

    public DebateStatus getStatus() {
        return status;
    }

    public User getCreator() {
        return creator;
    }

    public User getOpponent() {
        return opponent;
    }

    public Image getImage() {
        return image;
    }

    public Set<User> getSubscribedUsers() {
        return subscribedUsers;
    }

    public int getSubscribedUsersCount() {
        return subscribedUsersCount;
    }

    public int getForCount() {
        return (int) Math.round((forCount * 100.0) / (forCount + againstCount));
    }

    public int getAgainstCount() {
        return (int) Math.round((againstCount * 100.0) / (forCount + againstCount));

    }

    public boolean getIsCreatorFor() {
        return isCreatorFor;
    }
}
