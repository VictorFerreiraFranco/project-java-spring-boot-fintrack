
package io.github.fintrack.transaction.entry.controller.dto;

import io.github.fintrack.transaction.installment.controller.dto.InstallmentResponse;
import io.github.fintrack.transaction.transaction.model.Type;

import java.util.Map;
import java.util.UUID;

public record EntryTransactionByYearResponse(
    UUID id,
    Type type,
    String description,
    SingleCategoryResponse category,
    SingleMethodResponse method,
    Boolean recurrence,
    Map<Integer, InstallmentResponse> installments
) { }
