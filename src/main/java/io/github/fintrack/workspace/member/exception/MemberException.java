package io.github.fintrack.workspace.member.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;

public abstract class MemberException extends FinTrackMappedException {
    public MemberException(String message) {
        super(message);
    }
}
