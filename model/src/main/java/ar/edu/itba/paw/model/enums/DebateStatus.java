package ar.edu.itba.paw.model.enums;

public enum DebateStatus {
    OPEN("open"),
    CLOSED("closed"),
    DELETED("deleted");

    private final String status;

    DebateStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    public static DebateStatus getFromInt(int i) {
        return DebateStatus.values()[i];
    }
    public static int getFromStatus(DebateStatus status) {
        return status.ordinal();
    }
}
