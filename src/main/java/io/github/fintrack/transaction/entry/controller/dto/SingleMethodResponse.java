package io.github.fintrack.transaction.entry.controller.dto;

import io.github.fintrack.workspace.payment.method.model.Type;

public record SingleMethodResponse(
        String description,
        Type type
) {
}
