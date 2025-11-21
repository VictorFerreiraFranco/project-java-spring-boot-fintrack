package io.github.fintrack.workspace.invite.controller.contract;

import io.github.fintrack.auth.exception.UserNotFoundException;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.auth.service.UserService;
import io.github.fintrack.workspace.invite.controller.dto.InviteRequest;
import io.github.fintrack.workspace.invite.controller.dto.InviteResponse;
import io.github.fintrack.workspace.invite.controller.mapper.InviteResponseMapper;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.service.InviteService;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InviteContract {

    private final InviteService inviteService;
    private final WorkspaceService workspaceService;
    private final AuthService authService;
    private final UserService userService;
    private final InviteResponseMapper inviteResponseMapper;

    @Transactional(readOnly = true)
    public InviteResponse getById(String id) {
        return inviteResponseMapper.toDto(
                inviteService.findByIdAndValidateExistence(UUID.fromString(id))
        );
    }

    @Transactional(readOnly = true)
    public List<InviteResponse> getAllIsPendingByWorkspace(String workspaceId) {
        return inviteService.findAllIsPendingByWorkspace(
                        workspaceService.findByIdAndValidateExistenceAndMembership(UUID.fromString(workspaceId))
                )
                .stream()
                .map(inviteResponseMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InviteResponse> getAllIsPendingByUserLoggedIn() {
        return inviteService.findAllIsPendingByUser(authService.getUserLoggedIn())
                .stream()
                .map(inviteResponseMapper::toDto)
                .toList();
    }

    @Transactional
    public InviteResponse register(InviteRequest request) {
        User userTo = userService.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        Invite invite = Invite.builder()
                .from(authService.getUserLoggedIn())
                .to(userTo)
                .workspace(
                        workspaceService.findByIdAndValidateExistenceAndMembership(UUID.fromString(
                                request.workspaceId()
                        ))
                )
                .build();

        return inviteResponseMapper.toDto(
                inviteService.create(invite)
        );
    }

    @Transactional
    public void accept(String id) {
        inviteService.acceptInvite(
                inviteService.findByIdAndValidateExistence(UUID.fromString(id))
        );
    }

    @Transactional
    public void refused(String id) {
        inviteService.refuseInvite(
                inviteService.findByIdAndValidateExistence(UUID.fromString(id))
        );
    }

    @Transactional
    public void canceled(String id) {
        inviteService.canceledInvite(
                inviteService.findByIdAndValidateExistence(UUID.fromString(id))
        );
    }
}
