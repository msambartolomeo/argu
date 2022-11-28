package ar.edu.itba.paw.model.enums;

public enum DebateVote {
    FOR("for"),
    AGAINST("against");

    private final String name;

    DebateVote(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DebateVote getFromInt(int i) {
        return DebateVote.values()[i];
    }
}
