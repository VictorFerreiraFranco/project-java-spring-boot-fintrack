package io.github.fintrack.workspace.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkspaceRequest(
        @NotBlank(message = "field required")
        @Size(min = 2, max = 255, message = "the field must contain between {min} and {max} characters")
        String name
) {}