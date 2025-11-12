package io.github.fintrack.workspace.financial.category.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;

public abstract class FinancialCategoryException extends FinTrackMappedException {
    public FinancialCategoryException(String message) {
        super(message);
    }
}
