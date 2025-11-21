package io.github.fintrack.workspace.member.exception;

import io.github.fintrack.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MemberNotFoundInWorkspaceException extends ResourceNotFoundException {
    public MemberNotFoundInWorkspaceException() {
        super("Member not found in workspace");
    }
}
