package io.github.fintrack.workspace.payment.method.model;

import lombok.Getter;

@Getter
public enum Type {
    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito"),
    CASH("Dinheiro"),
    BANK_TRANSFER("Transferência Bancária"),
    PIX("PIX"),
    OTHER("Outro");

    private final String description;

    Type(String description) {this.description = description;}
}
