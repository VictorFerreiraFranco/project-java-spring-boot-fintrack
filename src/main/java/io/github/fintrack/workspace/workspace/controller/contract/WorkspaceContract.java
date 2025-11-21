package io.github.fintrack.workspace.workspace.controller.contract;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceMapper;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
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

    @Transactional(readOnly = true)
    public WorkspaceDetailsResponse getById(String id) {
        return workspaceMapper.toDetailsResponse(
                workspaceService.findByIdAndValidUserLoggedInIsMember(UUID.fromString(id))
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

    public WorkspaceSingleResponse update(String id, WorkspaceRequest request) {
        Workspace workspaceRequest = workspaceMapper.toEntity(request);

        Workspace workspace =  workspaceService.findByIdAndValidUserLoggedInIsMember(UUID.fromString(id));
        workspace.setName(workspaceRequest.getName());

        return workspaceMapper.toSingleResponse(
                workspaceService.save(workspace)
        );
    }

    public void delete(String id) {
        workspaceService.delete(
                workspaceService.findByIdAndValidUserLoggedInIsMember(UUID.fromString(id))
        );
    }
}
