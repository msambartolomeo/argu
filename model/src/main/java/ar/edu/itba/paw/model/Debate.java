package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;

import java.time.LocalDateTime;

public class Debate {
    private final long debateId;
    private final String name;
    private final String description;
    private final Long creatorId;
    private final Long opponentId;
    private final Long imageId;
    private final LocalDateTime createdDate;
    private final DebateCategory debateCategory;
    private final DebateStatus debateStatus;

    public Debate(long id, String name, String description, Long creatorId, Long opponentId, LocalDateTime createdDate, Long imageId, DebateCategory category, DebateStatus debateStatus) {
        this.debateId = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.opponentId = opponentId;
        this.createdDate = createdDate;
        this.imageId = imageId;
        this.debateCategory = category;
        this.debateStatus = debateStatus;
    }

    public Debate(long id, String name, String description, Long creatorId, Long opponentId, LocalDateTime createdDate, DebateCategory category, DebateStatus debateStatus) {
        this(id, name, description, creatorId, opponentId, createdDate, null, category, debateStatus);
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

    public DebateCategory getDebateCategory() {
        return debateCategory;
    }

    public DebateStatus getDebateStatus() {
        return debateStatus;
    }
}
