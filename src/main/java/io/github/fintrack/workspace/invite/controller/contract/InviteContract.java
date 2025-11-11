package io.github.fintrack.workspace.invite.controller.contract;

import io.github.fintrack.auth.exception.UserNotFoundException;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.auth.service.UserService;
import io.github.fintrack.workspace.invite.controller.dto.InviteRequest;
import io.github.fintrack.workspace.invite.controller.dto.InviteResponse;
import io.github.fintrack.workspace.invite.controller.mapper.InviteResponseMapper;
import io.github.fintrack.workspace.invite.exception.InviteNotFoundException;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.service.InviteService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
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
    public InviteResponse findById(String id) {
        return inviteService.findById(UUID.fromString(id))
                .map(inviteResponseMapper::toDto)
                .orElseThrow(InviteNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<InviteResponse> findAllIsPendingByWorkspace(String workspaceId) {
        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);

        return inviteService.findAllIsPendingByWorkspace(workspace)
                .stream()
                .map(inviteResponseMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InviteResponse> findAllIsPendingByUserLoggedIn() {
        return inviteService.findAllIsPendingByUser(authService.getUserLoggedIn())
                .stream()
                .map(inviteResponseMapper::toDto)
                .toList();
    }

    public InviteResponse register(InviteRequest request) {
        User userTo = userService.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(request.workspaceId()))
                .orElseThrow(WorkspaceNotFoundException::new);

        Invite invite = Invite.builder()
                .from(authService.getUserLoggedIn())
                .to(userTo)
                .workspace(workspace)
                .build();

        return inviteResponseMapper.toDto(
                inviteService.create(invite)
        );
    }

    public void accept(String id) {
        inviteService.acceptInvite(
                inviteService.findById(UUID.fromString(id))
                        .orElseThrow(InviteNotFoundException::new)
        );
    }

    public void refused(String id) {
        inviteService.refuseInvite(
                inviteService.findById(UUID.fromString(id))
                        .orElseThrow(InviteNotFoundException::new)
        );
    }

    public void canceled(String id) {
        inviteService.canceledInvite(
                inviteService.findById(UUID.fromString(id))
                        .orElseThrow(InviteNotFoundException::new)
        );
    }
}
