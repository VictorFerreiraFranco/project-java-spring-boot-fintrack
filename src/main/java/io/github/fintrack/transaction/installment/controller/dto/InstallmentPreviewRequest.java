package io.github.fintrack.transaction.installment.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentPreviewRequest(
        @NotNull
        @JsonProperty("start_date")
        LocalDate startDate,

        @NotNull
        @JsonProperty("total_installment")
        Integer totalInstallment,

        @NotBlank
        @PositiveOrZero
        BigDecimal amount
) {}