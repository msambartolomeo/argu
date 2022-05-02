package ar.edu.itba.paw.model.enums;

public enum DebateOrder {
    DATE,
    ALPHA,
    SUBS;

    DebateOrder() {
        this.isAscending = true;
    }
    private Boolean isAscending;
    public Boolean getOrder() {
        return isAscending;
    }
    public DebateOrder setAscending() {
        this.isAscending = true;
        return this;
    }
    public DebateOrder setDescending() {
        this.isAscending = false;
        return this;
    }
}
