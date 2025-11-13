package io.github.fintrack.workspace.payment.method.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.fintrack.workspace.payment.method.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record MethodResponse(
        UUID id,
        String description,
        Type type,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime created
) {
}
