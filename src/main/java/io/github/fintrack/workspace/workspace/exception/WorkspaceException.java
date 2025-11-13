package io.github.fintrack.workspace.workspace.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class WorkspaceException extends FinTrackMappedException {
    public WorkspaceException(String message) {
        super(message);
    }
}
