package io.github.fintrack.transaction.transaction.controller.dto;

import io.github.fintrack.transaction.transaction.model.Type;

public record TransactionTypeResponse(
        Type type,
        String description
) { }
