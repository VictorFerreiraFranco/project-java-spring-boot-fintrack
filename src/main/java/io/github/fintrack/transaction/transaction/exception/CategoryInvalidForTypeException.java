package io.github.fintrack.transaction.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CategoryInvalidForTypeException extends TransactionException {
    public CategoryInvalidForTypeException() {
        super("Category invalid for type of transaction");
    }
}
