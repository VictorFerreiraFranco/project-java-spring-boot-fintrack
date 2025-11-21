package io.github.fintrack.workspace.invite.service.validator;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.common.exception.DuplicateRecordException;
import io.github.fintrack.workspace.invite.exception.DeniedSendInviteYourselfException;
import io.github.fintrack.workspace.invite.exception.InviteIsNotPending;
import io.github.fintrack.workspace.invite.exception.UnauthorizedInviteException;
import io.github.fintrack.workspace.invite.exception.UserAlreadyRegisteredInWorkspaceException;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.invite.repository.InviteRepository;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InviteValidator {

    private final InviteRepository inviteRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    public void validToCreate(Invite invite) {
        if (inviteExists(invite))
            throw new DuplicateRecordException("Invite already exists");

        if (invite.getTo().getId().equals(invite.getFrom().getId()))
            throw new DeniedSendInviteYourselfException();

        if (userExistInWorkspace(invite))
            throw new UserAlreadyRegisteredInWorkspaceException();
    }

    public void validToAccept(Invite invite) {
        if (invite.getId() == null)
            throw new RuntimeException("Invite ID is required");

        if (!invite.isPending())
            throw new InviteIsNotPending();

        if (userLoggedInIsNotTo(invite))
            throw new UnauthorizedInviteException("unauthorized accept invite");
    }

    public void validToRefuse(Invite invite) {
        if (invite.getId() == null)
            throw new RuntimeException("Invite ID is required");

        if (!invite.isPending())
            throw new InviteIsNotPending();

        if (userLoggedInIsNotTo(invite))
            throw new UnauthorizedInviteException("unauthorized refuse invite");
    }

    public void validToCanceled(Invite invite) {
        if (invite.getId() == null)
            throw new RuntimeException("Invite ID is required");

        if (!invite.isPending())
            throw new InviteIsNotPending();

        if (userLoggedInIsNotFrom(invite))
            throw new UnauthorizedInviteException("unauthorized canceled invite");
    }

    public boolean inviteExists(Invite invite) {
        Optional<Invite> optionalInvite = inviteRepository.findByToAndWorkspaceAndStatus(invite.getTo(), invite.getWorkspace(), Status.PENDING);

        if (invite.getId() == null)
            return optionalInvite.isPresent();

        return optionalInvite.isPresent() && !optionalInvite.get().getId().equals(invite.getId());
    }

    public boolean userExistInWorkspace(Invite invite) {
        return memberRepository
                .findByWorkspaceAndUserAndDeletion_DeletedAtIsNull(invite.getWorkspace(), invite.getTo())
                .isPresent();
    }

    public boolean userLoggedInIsNotTo(Invite invite) {
        return !invite.getTo().getId().equals(authService.getUserLoggedIn().getId());
    }

    public boolean userLoggedInIsNotFrom(Invite invite) {
        return !invite.getFrom().getId().equals(authService.getUserLoggedIn().getId());
    }
}
