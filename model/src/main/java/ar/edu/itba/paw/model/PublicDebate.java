package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PublicDebate {
    private final long debateId;
    private final String name;
    private final String description;
    private final String creatorUsername;
    private final String opponentUsername;
    private final Long imageId;
    private final String createdDate;
    private final DebateCategory debateCategory;
    private final DebateStatus debateStatus;
    private final Integer subscribedUsers;

    private final Integer forCount;
    private final Integer againstCount;

    public PublicDebate(long debateId, String name, String description, String creatorUsername,
                        String opponentUsername, Long imageId, LocalDateTime createdDate,
                        DebateCategory debateCategory, Integer subscribedUsers, DebateStatus debateStatus,
                        Integer forCount, Integer againstCount) {
        this.debateId = debateId;
        this.name = name;
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.opponentUsername = opponentUsername;
        this.imageId = imageId;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));;
        this.debateCategory = debateCategory;
        this.subscribedUsers = subscribedUsers;
        this.debateStatus = debateStatus;
        this.forCount = (int) Math.round((forCount * 100.0) / (forCount + againstCount));
        this.againstCount = (int) Math.round((againstCount * 100.0) / (forCount + againstCount));
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

    public String getCreatedDate() {
        return createdDate;
    }

    public DebateCategory getDebateCategory() {
        return debateCategory;
    }

    public Integer getSubscribedUsers() {
        return subscribedUsers;
    }

    public DebateStatus getDebateStatus() {
        return debateStatus;
    }

    public Integer getForCount() {
        return forCount;
    }

    public Integer getAgainstCount() {
        return againstCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicDebate that = (PublicDebate) o;
        return debateId == that.debateId && name.equals(that.name) && description.equals(that.description)
                && creatorUsername.equals(that.creatorUsername) && Objects.equals(opponentUsername, that.opponentUsername)
                && Objects.equals(imageId, that.imageId) && createdDate.equals(that.createdDate) && debateCategory == that.debateCategory
                && debateStatus == that.debateStatus && subscribedUsers.equals(that.subscribedUsers) && forCount.equals(that.forCount)
                && againstCount.equals(that.againstCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debateId, name, description, creatorUsername, opponentUsername, imageId, createdDate, debateCategory, debateStatus, subscribedUsers, forCount, againstCount);
    }
}
