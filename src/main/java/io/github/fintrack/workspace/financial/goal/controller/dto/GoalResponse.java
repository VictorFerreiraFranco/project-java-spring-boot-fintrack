package io.github.fintrack.workspace.financial.goal.controller.dto;

import io.github.fintrack.workspace.financial.category.controller.dto.CategoryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GoalResponse(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDateTime created,
        CategoryResponse category
) { }
