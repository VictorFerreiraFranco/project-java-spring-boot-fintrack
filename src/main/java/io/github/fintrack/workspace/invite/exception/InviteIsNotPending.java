package io.github.fintrack.workspace.invite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class InviteIsNotPending extends InviteException {
    public InviteIsNotPending() {
        super("Invite is not pending");
    }
}
