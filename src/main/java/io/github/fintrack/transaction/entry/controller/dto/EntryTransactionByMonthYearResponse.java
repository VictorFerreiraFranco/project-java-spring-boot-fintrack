package io.github.fintrack.transaction.entry.controller.dto;

import io.github.fintrack.transaction.transaction.model.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EntryTransactionByMonthYearResponse(
    UUID id,
    Type type,
    String description,
    SingleCategoryResponse category,
    SingleMethodResponse method,
    Boolean recurrence,
    LocalDate date,
    BigDecimal amount
) { }
