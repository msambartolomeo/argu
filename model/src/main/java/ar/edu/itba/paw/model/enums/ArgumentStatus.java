package ar.edu.itba.paw.model.enums;

public enum ArgumentStatus {
    INTRODUCTION("introduction"),
    ARGUMENT("argument"),
    CONCLUSION("conclusion");

    private final String name;

    ArgumentStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static ArgumentStatus getFromInt(int i) {
        return ArgumentStatus.values()[i];
    }
    public static int getFromStatus(ArgumentStatus status) {
        return status.ordinal();
    }
}
