package io.github.fintrack.workspace.financial.category.controller.dto;

import io.github.fintrack.transaction.transaction.model.Type;

public record CategoryTypeResponse (
    Type type,
    String description
) { }
