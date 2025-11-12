package io.github.fintrack.workspace.invite.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;

public abstract class InviteException extends FinTrackMappedException {
    public InviteException(String message) {
        super(message);
    }
}
