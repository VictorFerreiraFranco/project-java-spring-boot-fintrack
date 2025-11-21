package io.github.fintrack.workspace.payment.method.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.fintrack.workspace.payment.method.model.Type;

import java.time.LocalDateTime;
import java.util.UUID;

public record MethodResponse(
        UUID id,
        Type type,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime created
) {
}
