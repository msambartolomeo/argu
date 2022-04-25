package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class Debate {
    private final long debateId;
    private final String name;
    private final String description;
    private final Long imageId;
    private final LocalDateTime createdDate;
    private final DebateCategory debateCategory;

    public Debate(long id, String name, String description, LocalDateTime createdDate, Long imageId, DebateCategory category) {
        this.debateId = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.imageId = imageId;
        this.debateCategory = category;
    }

    public Debate(long id, String name, String description, LocalDateTime createdDate, DebateCategory category) {
        this(id, name, description, createdDate, null, category);
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Long getImageId() {
        return imageId;
    }

    public DebateCategory getCategory() {
        return debateCategory;
    }
}
