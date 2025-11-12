package io.github.fintrack.workspace.workspace.controller.contract;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceMapper;
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
public class WorkspaceContract {

    private final WorkspaceService workspaceService;
    private final AuthService authService;
    private final WorkspaceMapper workspaceMapper;

    @Transactional(readOnly = true)
    public WorkspaceDetailsResponse findById(String id) {
        return workspaceMapper.toDetailsResponse(
            workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .orElseThrow(WorkspaceNotFoundException::new)
        );
    }

    @Transactional(readOnly = true)
    public List<WorkspaceDetailsResponse> findByUserLoggedId() {
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
        return workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .map(workspace -> {
                    Workspace workspaceRequest = workspaceMapper.toEntity(request);

                    workspace.setName(workspaceRequest.getName());
                    workspaceService.save(workspace);

                    return workspaceMapper.toSingleResponse(workspace);
                })
                .orElseThrow(WorkspaceNotFoundException::new);
    }

    public void delete(String id) {
        workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .map(workspace -> {
                    workspaceService.delete(workspace);
                    return workspace;
                })
                .orElseThrow(WorkspaceNotFoundException::new);
    }
}
