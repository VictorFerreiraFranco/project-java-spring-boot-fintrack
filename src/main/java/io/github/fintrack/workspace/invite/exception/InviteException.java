package io.github.fintrack.workspace.invite.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class InviteException extends FinTrackMappedException {
    public InviteException(String message) {
        super(message);
    }
}
