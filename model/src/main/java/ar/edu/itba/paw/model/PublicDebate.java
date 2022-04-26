package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateCategory;

import java.time.LocalDateTime;

public class PublicDebate {
    private final long debateId;
    private final String name;
    private final String description;
    private final String creatorUsername;
    private final String opponentUsername;
    private final Long imageId;
    private final LocalDateTime createdDate;
    private final DebateCategory debateCategory;
    private final Integer subscribedUsers;

    public PublicDebate(long debateId, String name, String description, String creatorUsername, String opponentUsername, Long imageId, LocalDateTime createdDate, DebateCategory debateCategory, Integer subscribedUsers) {
        this.debateId = debateId;
        this.name = name;
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.opponentUsername = opponentUsername;
        this.imageId = imageId;
        this.createdDate = createdDate;
        this.debateCategory = debateCategory;
        this.subscribedUsers = subscribedUsers;
    }

    public PublicDebate(long debateId, String name, String description, String creatorUsername, String opponentUsername, LocalDateTime createdDate, DebateCategory debateCategory, Integer subscribedUsers) {
        this(debateId, name, description, creatorUsername, opponentUsername, null, createdDate, debateCategory, subscribedUsers);
    }

    public long getDebateId() {
        return debateId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public Long getImageId() {
        return imageId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public DebateCategory getDebateCategory() {
        return debateCategory;
    }

    public Integer getSubscribedUsers() {
        return subscribedUsers;
    }
}
