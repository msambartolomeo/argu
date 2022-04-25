package ar.edu.itba.paw.model;

public enum DebateCategory {
    CULTURE,
    ECONOMICS,
    EDUCATION,
    ENTERTAINMENT,
    HISTORY,
    LITERATURE,
    POLITICS,
    RELIGION,
    SCIENCE,
    TECHNOLOGY,
    WORLD;

    // Get category from integer
    public static DebateCategory getFromInt(Integer i) {
        return DebateCategory.values()[i];
    }
    public static Integer getFromCategory(DebateCategory category) {
        return category.ordinal();
    }
}
