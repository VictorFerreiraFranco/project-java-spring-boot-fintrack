package io.github.fintrack.workspace.financial.category.controller.dto;

import io.github.fintrack.transaction.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryUpdateRequest {
    @NotBlank
    @Size(min = 3, max = 255)
    public String description;

    @NotBlank
    @Size(min = 1, max = 255)
    public String color;

    @NotBlank
    public Type type;
}
