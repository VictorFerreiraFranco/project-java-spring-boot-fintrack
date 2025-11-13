package io.github.fintrack.workspace.financial.goal.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;

import java.math.BigDecimal;

public record GoalFilter (
    String description,
    BigDecimal amount,

    @ValidUUID
    @JsonProperty("category_id")
    String categoryId
) { }
