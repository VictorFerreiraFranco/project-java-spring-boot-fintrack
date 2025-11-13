package io.github.fintrack.workspace.payment.method.controller.dto;

import io.github.fintrack.workspace.payment.method.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MethodRequest(
        @NotBlank
        @Size(min = 3, max = 255)
        String description,

        @NotBlank
        @Size(min = 3, max = 255)
        Type type
) {
}
