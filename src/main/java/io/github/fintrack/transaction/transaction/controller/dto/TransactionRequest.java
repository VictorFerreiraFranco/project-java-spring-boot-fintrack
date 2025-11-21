package io.github.fintrack.transaction.transaction.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.transaction.transaction.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull
        Type type,

        @NotBlank
        @Size(min = 1, max = 255)
        String description,

        @NotNull
        @PositiveOrZero
        BigDecimal amount,

        @NotNull
        @JsonProperty("start_date")
        LocalDate startDate,

        @NotNull
        @JsonProperty("total_installment")
        Integer totalInstallment,

        @NotBlank
        @ValidUUID
        @JsonProperty("category_id")
        String categoryId,

        @NotBlank
        @ValidUUID
        @JsonProperty("method_id")
        String methodId,

        String observation
) {}
