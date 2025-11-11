package io.github.fintrack.workspace.invite.exception;

import io.github.fintrack.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InviteNotFoundException extends ResourceNotFoundException {
    public InviteNotFoundException() {
        super("Invite not found");
    }
}
