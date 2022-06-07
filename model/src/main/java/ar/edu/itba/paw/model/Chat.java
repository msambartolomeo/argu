package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_chatid_seq")
    @SequenceGenerator(sequenceName = "chats_chatid_seq", name = "chats_chatid_seq", allocationSize = 1)
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debateid")
    private Debate debate;

    @Lob
    @Column(nullable = false)
    private String message;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime creationDate;

    Chat() {}

    public Chat(User user, Debate debate, String message) {
        this.user = user;
        this.debate = debate;
        this.message = message;
        this.creationDate = LocalDateTime.now();
    }

    public Long getChatId() {
        return chatId;
    }

    public User getUser() {
        return user;
    }

    public Debate getDebate() {
        return debate;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getFormattedDate() {
        return creationDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
    }
}
