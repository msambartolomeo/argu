package ar.edu.itba.paw.model;

public class Debate {
    private final long debateId;
    private final String name;
    private final String description;

    public Debate(long id, String name, String description) {
        this.debateId = id;
        this.name = name;
        this.description = description;
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
}
