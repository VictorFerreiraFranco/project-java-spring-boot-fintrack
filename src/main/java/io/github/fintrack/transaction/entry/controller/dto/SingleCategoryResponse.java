package io.github.fintrack.transaction.entry.controller.dto;

import io.github.fintrack.transaction.transaction.model.Type;

public record SingleCategoryResponse(
    String description,
    String color,
    Type type
) { }
