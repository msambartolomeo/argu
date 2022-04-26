package ar.edu.itba.paw.model.enums;

public enum DebateStatus {
    OPEN("open"),
    CLOSED("closed"),
    DELETED("deleted");

    private final String name;

    DebateStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static DebateStatus getFromInt(int i) {
        return DebateStatus.values()[i];
    }
    public static int getFromStatus(DebateStatus status) {
        return status.ordinal();
    }
}
