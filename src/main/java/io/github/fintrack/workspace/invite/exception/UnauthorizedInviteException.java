package io.github.fintrack.workspace.invite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedInviteException extends InviteException {
    public UnauthorizedInviteException(String message) {
        super(message);
    }
}
