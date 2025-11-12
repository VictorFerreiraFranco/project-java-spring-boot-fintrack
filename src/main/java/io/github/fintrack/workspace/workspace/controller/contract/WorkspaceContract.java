package io.github.fintrack.workspace.workspace.controller.contract;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceMapper;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorkspaceContract {

    private final WorkspaceService workspaceService;
    private final AuthService authService;
    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceValidator workspaceValidator;

    @Transactional(readOnly = true)
    public WorkspaceDetailsResponse getById(String id) {
        return workspaceMapper.toDetailsResponse(
            this.findById(id)
        );
    }

    @Transactional(readOnly = true)
    public List<WorkspaceDetailsResponse> getByUserLoggedId() {
        return workspaceService.findAllByMembersUserAndDeletedAtIsNull(authService.getUserLoggedIn())
                .stream()
                .map(workspaceMapper::toDetailsResponse)
                .toList();
    }

    public WorkspaceSingleResponse create(WorkspaceRequest request) {
        return workspaceMapper.toSingleResponse(
            workspaceService.createOther(
                request.name(),
                authService.getUserLoggedIn()
            )
        );
    }

    @Transactional
    public WorkspaceSingleResponse update(String id, WorkspaceRequest request) {
        Workspace workspaceRequest = workspaceMapper.toEntity(request);

        Workspace workspace = this.findById(id);
        workspace.setName(workspaceRequest.getName());

        return workspaceMapper.toSingleResponse(
                workspaceService.save(workspace)
        );
    }

    @Transactional
    public void delete(String id) {
        workspaceService.delete(
                this.findById(id)
        );
    }

    private Workspace findById(String id) {
        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .orElseThrow(WorkspaceNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(workspace);

        return workspace;
    }
}
