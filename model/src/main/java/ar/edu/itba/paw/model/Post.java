package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.ArgumentStatus;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts2")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_postid_seq")
    @SequenceGenerator(sequenceName = "posts_postid_seq", name = "posts_postid_seq", allocationSize = 1)
    @Column(name = "postid")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debateid")
    private Debate debate;

    @Column(nullable = false)
    private String content;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Image image;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false, length = 20)
    private ArgumentStatus status;

    @Formula("(SELECT COUNT(*) FROM likes WHERE postid = postid)")
    private int likesCount;

    /*default*/Post() {
        // Just for Hibernate
    }

    public Post(final User user, final Debate debate, final String content, final Image image, final ArgumentStatus status) {
        this.debate = debate;
        this.user = user;
        this.content = content;
        this.creationDate = LocalDateTime.now();
        this.image = image;
        this.status = status;
        this.likesCount = 0;
    }

    @Deprecated
    public Post(final long postId, final User user, final Long debateId, final String content, final LocalDateTime creationDate, final Image image, final ArgumentStatus status) {

    }

    public Long getPostId() {
        return postId;
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

    public Image getImage() {
        return image;
    }

    public ArgumentStatus getStatus() {
        return status;
    }

    public int getLikesCount() {
        return likesCount;
    }
}
