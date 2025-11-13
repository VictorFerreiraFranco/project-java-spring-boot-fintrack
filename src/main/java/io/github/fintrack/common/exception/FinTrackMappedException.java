package io.github.fintrack.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FinTrackMappedException extends RuntimeException {
    public FinTrackMappedException(String message) {
        super(message);
    }
}
