package io.github.fintrack.transaction.transaction.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class TransactionException extends FinTrackMappedException {
    public TransactionException(String message) {
        super(message);
    }
}
