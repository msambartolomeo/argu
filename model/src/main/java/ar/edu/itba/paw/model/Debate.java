package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class Debate {
    private final long debateId;
    private final String name;
    private final String description;

    private final LocalDateTime createdDate;

    public Debate(long id, String name, String description, LocalDateTime createdDate) {
        this.debateId = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
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
}
