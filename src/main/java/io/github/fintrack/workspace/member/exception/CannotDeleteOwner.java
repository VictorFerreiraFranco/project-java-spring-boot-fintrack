package io.github.fintrack.workspace.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CannotDeleteOwner extends MemberWorkspaceException {
    public CannotDeleteOwner() {
        super("It is not possible to remove the workspace owner.");
    }
}
