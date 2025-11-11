package io.github.fintrack.workspace.invite.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.invite.repository.InviteRepository;
import io.github.fintrack.workspace.invite.validator.InviteValidator;
import io.github.fintrack.workspace.member.service.MemberService;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final InviteValidator inviteValidator;
    private final MemberService memberService;

    public Optional<Invite> findById(UUID id) {
        return  inviteRepository.findById(id);
    }

    public List<Invite> findAllIsPendingByWorkspace(Workspace workspace) {
        return inviteRepository.findAllByWorkspaceAndStatus(workspace, Status.PENDING);
    }

    public List<Invite> findAllIsPendingByUser(User user) {
        return inviteRepository.findAllByStatusAndTo(Status.PENDING, user);
    }

    @Transactional
    public Invite create(Invite invite) {
        invite.setStatus(Status.PENDING);
        inviteValidator.validToCreate(invite);
        return inviteRepository.save(invite);
    }

    @Transactional
    public void acceptInvite(Invite invite) {
        inviteValidator.validToAccept(invite);
        invite.setStatus(Status.ACCEPTED);
        inviteRepository.save(invite);

        memberService.createByInvite(invite);
    }

    public void refuseInvite(Invite invite) {
        inviteValidator.validToRefuse(invite);
        invite.setStatus(Status.REFUSED);
        inviteRepository.save(invite);
    }

    public void canceledInvite(Invite invite) {
        inviteValidator.validToCanceled(invite);
        invite.setStatus(Status.CANCELED);
        inviteRepository.save(invite);
    }
}
