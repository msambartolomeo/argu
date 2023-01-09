package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateResult;
import ar.edu.itba.paw.model.enums.DebateStatus;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Type(type = "org.hibernate.type.TextType")
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

    @Column(name = "date_to_close")
    private LocalDate dateToClose;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category", nullable = false, length = 20)
    private DebateCategory category;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false, length = 20)
    private DebateStatus status;

    public void setStatus(DebateStatus status) {
        this.status = status;
    }

    public void deleteDebate() {
        this.status = DebateStatus.DELETED;
        this.image = null;
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

    public Long getDebateId() {
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

    public LocalDate getDateToClose() {
        return dateToClose;
    }

    public String getFormattedDateToClose() {
        return dateToClose.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setDateToClose(LocalDate dateToClose) {
        this.dateToClose = dateToClose;
    }

    public void closeDebate() {
        if (this.status == DebateStatus.VOTING)
            addPointsToParticipants();
        this.status = DebateStatus.CLOSED;
    }

    public void startVoting() {
        // 3 días después de cerrar un debate, se cierra la votación.
        this.dateToClose = LocalDate.now().plusDays(3);
        this.status = DebateStatus.VOTING;
    }

    public DebateResult getDebateResult() {
        if (forCount == againstCount)
            return DebateResult.DRAW;
        else if (forCount > againstCount)
            return DebateResult.FOR;
        else
            return DebateResult.AGAINST;
    }

    private void addPointsToParticipants() {
        int totalPoints = forCount + againstCount;
        DebateResult result = getDebateResult();
        User winner;
        User loser;
        switch (result) {
            case FOR:
                winner = creator;
                loser = opponent;
                break;
            case AGAINST:
                winner = opponent;
                loser = creator;
                break;
            case DRAW:
            default:
                creator.addDrawPoints(totalPoints);
                opponent.addDrawPoints(totalPoints);
                return;
        }
        winner.addWinPoints(totalPoints);
        loser.addLosePoints(totalPoints);
    }
}
