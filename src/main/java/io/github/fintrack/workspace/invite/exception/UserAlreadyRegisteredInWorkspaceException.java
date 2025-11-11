package io.github.fintrack.workspace.invite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyRegisteredInWorkspaceException extends InviteException {
    public UserAlreadyRegisteredInWorkspaceException() {
        super("User already registered in the workspace.");
    }
}
