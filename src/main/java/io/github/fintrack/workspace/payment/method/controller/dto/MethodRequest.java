package io.github.fintrack.workspace.payment.method.controller.dto;

import io.github.fintrack.workspace.payment.method.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MethodRequest(
        @NotNull
        @Size(min = 3, max = 255)
        Type type,

        @NotBlank
        @Size(min = 3, max = 255)
        String description
) {
}
