package ar.edu.itba.paw.model.enums;

public enum DebateOrder {
    DATE_ASC("date_asc"),
    DATE_DESC("date_desc"),
    ALPHA_ASC("alpha_asc"),
    ALPHA_DESC("alpha_desc"),
    SUBS_ASC("subs_asc"),
    SUBS_DESC("subs_desc");
    private final String name;
    DebateOrder(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
