package io.github.fintrack.workspace.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class CannotPermissionDelete extends MemberException {
    public CannotPermissionDelete() {
        super("You do not have permission to delete.");
    }
}
