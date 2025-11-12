package io.github.fintrack.workspace.workspace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserIsNotMemberWorkspaceException extends WorkspaceException {
    public UserIsNotMemberWorkspaceException() {
        super("User is not member");
    }
}
