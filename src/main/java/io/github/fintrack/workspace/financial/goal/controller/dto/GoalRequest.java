package io.github.fintrack.workspace.financial.goal.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record GoalRequest (
        @NotBlank
        @Size(min = 3, max = 255)
        String description,

        @NotNull
        BigDecimal amount
) { }
