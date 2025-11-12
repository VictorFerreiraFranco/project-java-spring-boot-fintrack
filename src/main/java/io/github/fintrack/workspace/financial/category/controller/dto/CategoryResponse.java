package io.github.fintrack.workspace.financial.category.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse (
    UUID id,
    String description,
    String color,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created
) { }
