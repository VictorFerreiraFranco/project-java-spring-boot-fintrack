package io.github.fintrack.workspace.workspace.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;

public abstract class WorkspaceException extends FinTrackMappedException {
    public WorkspaceException(String message) {
        super(message);
    }
}
