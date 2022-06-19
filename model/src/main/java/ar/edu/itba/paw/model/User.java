package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.UserRole;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid_seq")
    @SequenceGenerator(allocationSize = 1, name = "users_userid_seq", sequenceName = "users_userid_seq")
    private Long userId;
    @Column(length = 100, unique = true)
    private String email;
    @Column(length = 64, unique = true)
    private String username;
    @Column(length = 100)
    private String password;
    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "imageid")
    private Image image;
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<Debate> createdDebates;

    @OneToMany(mappedBy = "opponent", fetch = FetchType.LAZY)
    private Set<Debate> opponentDebates;

    @Column(name = "points", columnDefinition = "integer default 0")
    private int points;

    @Column(name = "locale", nullable = false, columnDefinition = "varchar(255) default 'en'")
    private Locale locale;

    public User() {}

    public User(String email, String username, String password, Locale locale) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdDate = LocalDate.now();
        this.role = UserRole.USER;
        this.points = 0;
        this.locale = locale;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getFormattedDate() {
        return createdDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public Image getImage() {
        return image;
    }
    public UserRole getRole() {
        return role;
    }

    public Locale getLocale() {
        return locale;
    }

    public void updateImage(byte[] data) {
        this.image = new Image(data);
    }

    public User updateLegacyUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdDate = LocalDate.now();
        this.role = UserRole.USER;
        return this;
    }

    public void removeUser() {
        this.email = null;
        this.username = null;
        this.password = null;
        this.role = UserRole.USER;
        for (Debate debate : createdDebates) {
            if (debate.getStatus() != DebateStatus.DELETED && debate.getStatus() != DebateStatus.CLOSED)
                debate.closeDebate();
        }
        for (Debate debate : opponentDebates) {
            if (debate.getStatus() != DebateStatus.DELETED && debate.getStatus() != DebateStatus.CLOSED)
                debate.closeDebate();
        }
    }

    // TODO: Choose correct amount of points
    public int getPoints() {
        return points;
    }
    public void addLikePoints() {
        this.points += 1;
    }
    public void removeLikePoints() {
        this.points -= 1;
    }
    public void addSubPoints() {
        this.points += 1;
    }
    public void removeSubPoints() {
        this.points -= 1;
    }
    public void addWinPoints(int totalVotes) {
        this.points += 10*totalVotes;
    }
    public void addLosePoints(int totalVotes) {
        this.points += 5*totalVotes;
    }

    public void addDrawPoints(int totalVotes) {
        this.points += 7*totalVotes;
    }
}
