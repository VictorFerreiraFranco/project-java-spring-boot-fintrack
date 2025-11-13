package io.github.fintrack.workspace.payment.method.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class PaymentMethodException extends FinTrackMappedException {
    public PaymentMethodException(String message) {
        super(message);
    }
}
