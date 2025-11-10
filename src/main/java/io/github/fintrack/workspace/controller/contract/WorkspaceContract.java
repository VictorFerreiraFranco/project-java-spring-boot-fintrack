package io.github.fintrack.workspace.controller.contract;

import io.github.fintrack._common.exception.ResourceNotFoundException;
import io.github.fintrack.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.controller.mapper.WorkspaceRequestMapper;
import io.github.fintrack.workspace.model.Workspace;
import io.github.fintrack.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorkspaceContract {

    private final WorkspaceRequestMapper workspaceRequestMapper;
    private final WorkspaceService  workspaceService;

    public void create(WorkspaceRequest request) {
        Workspace workspace = workspaceRequestMapper.toEntity(request);
        workspaceService.save(workspace);
    }

    public  void update(String id, WorkspaceRequest request) {
        Workspace workspaceRequest = workspaceRequestMapper.toEntity(request);

        workspaceService.findById(UUID.fromString(id))
                .map(workspace -> {
                    workspace.setName(workspaceRequest.getName());
                    workspaceService.save(workspace);

                    return workspace;
                })
                .orElseThrow(this::getWorkspaceNotFoundException);
    }

    public void delete(String id) {
        workspaceService.findById(UUID.fromString(id))
                .map(workspace -> {
                    workspaceService.delete(workspace);
                    return workspace;
                })
                .orElseThrow(this::getWorkspaceNotFoundException);
    }

    private ResourceNotFoundException getWorkspaceNotFoundException() {
        return new ResourceNotFoundException("Workspace not found");
    }
}
