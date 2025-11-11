package io.github.fintrack.workspace.member.exception;

import io.github.fintrack.common.exception.ResourceNotFoundException;

public class MemberNotFoundException extends ResourceNotFoundException {
    public MemberNotFoundException() {
        super("Member not found");
    }
}
