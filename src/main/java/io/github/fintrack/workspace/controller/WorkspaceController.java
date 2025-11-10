package io.github.fintrack.workspace.controller;

import io.github.fintrack._common.controller.GenericController;
import io.github.fintrack.workspace.controller.contract.WorkspaceContract;
import io.github.fintrack.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.controller.dto.WorkspaceResponse;
import io.github.fintrack.workspace.controller.mapper.WorkspaceResponseMapper;
import io.github.fintrack.workspace.model.Workspace;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController implements GenericController {

    private final WorkspaceContract contract;
    private final WorkspaceResponseMapper workspaceResponseMapper;

    @GetMapping("{id}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(
            @PathVariable String id
    ) {
        Workspace workspace = contract.getWorkspace(id);
        return ResponseEntity
                .ok(workspaceResponseMapper.toDto(workspace));
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponse> create(
            @Valid @RequestBody WorkspaceRequest request
    ){
        Workspace workspace = contract.create(request);
        return ResponseEntity
                .created(buildHeaderLocation(workspace.getId()))
                .body(workspaceResponseMapper.toDto(workspace));
    }

    @PatchMapping("{id}")
    public ResponseEntity<WorkspaceResponse> update(
            @PathVariable String id,
            @Valid @RequestBody WorkspaceRequest request
    ){
        Workspace workspace = contract.update(id, request);
        return ResponseEntity
                .ok(workspaceResponseMapper.toDto(workspace));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ){
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }
}
