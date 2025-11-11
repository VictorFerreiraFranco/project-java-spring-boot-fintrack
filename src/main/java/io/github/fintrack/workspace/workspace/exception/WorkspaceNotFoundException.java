package io.github.fintrack.workspace.workspace.exception;

import io.github.fintrack.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WorkspaceNotFoundException extends ResourceNotFoundException {
    public WorkspaceNotFoundException() {
        super("Workspace not found");
    }
}
