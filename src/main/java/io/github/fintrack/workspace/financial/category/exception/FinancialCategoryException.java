package io.github.fintrack.workspace.financial.category.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class FinancialCategoryException extends FinTrackMappedException {
    public FinancialCategoryException(String message) {
        super(message);
    }
}
