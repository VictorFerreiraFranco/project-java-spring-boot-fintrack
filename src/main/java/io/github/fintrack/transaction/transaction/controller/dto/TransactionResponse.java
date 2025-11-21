package io.github.fintrack.transaction.transaction.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.fintrack.transaction.installment.controller.dto.InstallmentResponse;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryResponse;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TransactionResponse (
        UUID id,
        Type type,
        WorkspaceSingleResponse workspace,
        CategoryResponse category,
        MethodResponse method,
        String description,
        BigDecimal amount,
        @JsonProperty("amount_installment")
        BigDecimal amountInstallment,
        LocalDate startDate,
        LocalDate endDate,
        @JsonProperty("total_installment")
        Integer totalInstallment,
        Boolean recurrence,
        String observation,
        List<InstallmentResponse> installments
) {}
