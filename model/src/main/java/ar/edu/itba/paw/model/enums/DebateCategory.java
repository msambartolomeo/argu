package ar.edu.itba.paw.model.enums;

public enum DebateCategory {
    CULTURE("culture"),
    ECONOMICS("economics"),
    EDUCATION("education"),
    ENTERTAINMENT("entertainment"),
    HISTORY("history"),
    LITERATURE("literature"),
    POLITICS("politics"),
    RELIGION("religion"),
    SCIENCE("science"),
    TECHNOLOGY("technology"),
    WORLD("world"),
    OTHER("other");

    private final String name;

    DebateCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DebateCategory getFromInt(Integer i) {
        return DebateCategory.values()[i];
    }
    public static Integer getFromCategory(DebateCategory category) {
        return category.ordinal();
    }
}
