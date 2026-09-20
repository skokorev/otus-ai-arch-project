package ru.yahoondex.archhelper.commons.contracts.kafka;

public enum Operation {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String name;

    private Operation(String name) {
        this.name = name;
    }
}
