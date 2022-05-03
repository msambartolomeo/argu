package ar.edu.itba.paw.model.enums;

public enum DebateOrder {
    DATE_ASC("date.asc"),
    DATE_DESC("date.desc"),
    ALPHA_ASC("alpha.asc"),
    ALPHA_DESC("alpha.desc"),
    SUBS_ASC("subs.asc"),
    SUBS_DESC("subs.desc");
    private final String name;
    DebateOrder(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
