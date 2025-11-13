package io.github.fintrack.workspace.payment.method.exception;

import io.github.fintrack.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PaymentMethodNotFoundException extends ResourceNotFoundException {
    public PaymentMethodNotFoundException() {
        super("Payment method not found");
    }
}
