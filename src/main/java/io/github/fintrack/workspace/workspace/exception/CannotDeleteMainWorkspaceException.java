package io.github.fintrack.workspace.workspace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class CannotDeleteMainWorkspaceException extends WorkspaceException {
    public CannotDeleteMainWorkspaceException() {
        super("You cannot delete the main workspace.");
    }
}
