package io.github.fintrack.transaction.transaction.model;

import lombok.Getter;

@Getter
public enum Type {
    EXPENSE("Despesa"),
    INCOME("Receita");

    private final String description;

    Type(String description) {
        this.description = description;
    }
}
