package io.github.fintrack.workspace.workspace.controller.contract;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceResponse;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceRequestMapper;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceResponseMapper;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorkspaceContract {

    private final WorkspaceRequestMapper workspaceRequestMapper;
    private final WorkspaceResponseMapper workspaceResponseMapper;
    private final WorkspaceService workspaceService;
    private final AuthService authService;

    public WorkspaceResponse findById(String id) {
        return workspaceResponseMapper.toDto(
            workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .orElseThrow(WorkspaceNotFoundException::new)
        );
    }

    public List<WorkspaceResponse> findByUserLoggedId() {
        return workspaceService.findAllByMembersUserAndDeletedAtIsNull(authService.getUserLoggedIn())
                .stream()
                .map(workspaceResponseMapper::toDto)
                .toList();
    }

    public WorkspaceResponse create(WorkspaceRequest request) {
        return workspaceResponseMapper.toDto(
            workspaceService.createOther(
                request.name(),
                authService.getUserLoggedIn()
            )
        );
    }

    public WorkspaceResponse update(String id, WorkspaceRequest request) {
        return workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .map(workspace -> {
                    Workspace workspaceRequest = workspaceRequestMapper.toEntity(request);

                    workspace.setName(workspaceRequest.getName());
                    workspaceService.save(workspace);

                    return workspaceResponseMapper.toDto(workspace);
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
