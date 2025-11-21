package io.github.fintrack.transaction.installment.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentResponse(
        LocalDate date,
        BigDecimal amount,
        Integer installment
) {}