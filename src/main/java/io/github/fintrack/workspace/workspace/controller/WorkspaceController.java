package io.github.fintrack.workspace.workspace.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.workspace.workspace.controller.contract.WorkspaceContract;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController implements GenericController {

    private final WorkspaceContract contract;

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDetailsResponse> getById(
            @ValidUUID @PathVariable String id
    ) {
        return ResponseEntity.ok(contract.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<WorkspaceDetailsResponse>> getByUserLoggedId() {
        return ResponseEntity.ok(contract.getByUserLoggedId());
    }

    @PostMapping
    public ResponseEntity<WorkspaceSingleResponse> create(
            @Valid @RequestBody WorkspaceRequest request
    ){
        WorkspaceSingleResponse workspaceResponse = contract.create(request);

        return ResponseEntity
                .created(buildHeaderLocation(workspaceResponse.id()))
                .body(workspaceResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkspaceSingleResponse> update(
            @ValidUUID @PathVariable String id,
            @Valid @RequestBody WorkspaceRequest request
    ){
        return ResponseEntity
                .ok(contract.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @ValidUUID @PathVariable String id
    ){
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }
}
