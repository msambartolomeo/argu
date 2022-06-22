package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.ArgumentStatus;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "posts")
public class Argument {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_postid_seq")
    @SequenceGenerator(sequenceName = "posts_postid_seq", name = "posts_postid_seq", allocationSize = 1)
    @Column(name = "postid")
    private Long argumentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debateid")
    private Debate debate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(nullable = false)
    private String content;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageid")
    private Image image;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false, length = 20)
    private ArgumentStatus status;

    @Formula("(SELECT COUNT(*) FROM likes WHERE likes.postid = postid)")
    private int likesCount;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted;

    @Transient
    private boolean isLikedByUser;

    Argument() {}

    public Argument(final User user, final Debate debate, final String content, final Image image, final ArgumentStatus status) {
        this.debate = debate;
        this.user = user;
        this.content = content;
        this.creationDate = LocalDateTime.now();
        this.image = image;
        this.status = status;
        this.likesCount = 0;
        this.deleted = false;
        this.isLikedByUser = false;
    }

    public Long getArgumentId() {
        return argumentId;
    }

    public User getUser() {
        return user;
    }

    public Debate getDebate() {
        return debate;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public String getFormattedDate() {
        return creationDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
    }

    public Image getImage() {
        return image;
    }

    public ArgumentStatus getStatus() {
        return status;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        isLikedByUser = likedByUser;
    }

    public void deleteArgument() {
        this.deleted = true;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }
}
