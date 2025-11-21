package io.github.fintrack.workspace.financial.category.controller.dto;

import io.github.fintrack.transaction.transaction.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest (
        @NotBlank
        @Size(min = 3, max = 255)
        String description,

        @NotBlank
        @Size(min = 1, max = 255)
        String color,

        @NotNull
        Type type
) {}
