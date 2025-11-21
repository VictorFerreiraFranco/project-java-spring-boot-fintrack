package io.github.fintrack.transaction.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends TransactionException {
    public TransactionNotFoundException() {
        super("Transaction not found");
    }
}
