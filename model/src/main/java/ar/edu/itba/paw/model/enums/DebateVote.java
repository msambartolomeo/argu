package ar.edu.itba.paw.model.enums;

public enum DebateVote {
    FOR,
    AGAINST;

    public static DebateVote getFromInt(int i) {
        return DebateVote.values()[i];
    }
}
