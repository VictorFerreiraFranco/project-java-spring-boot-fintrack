package io.github.fintrack.workspace.workspace.controller;

import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.workspace.workspace.controller.contract.WorkspaceContract;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceResponse;
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

    @GetMapping("{id}")
    public ResponseEntity<WorkspaceResponse> findById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(contract.findById(id));
    }

    @GetMapping("my")
    public ResponseEntity<List<WorkspaceResponse>> findByUserLoggedId() {
        return ResponseEntity.ok(contract.findByUserLoggedId());
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponse> create(
            @Valid @RequestBody WorkspaceRequest request
    ){
        WorkspaceResponse workspaceResponse = contract.create(request);

        return ResponseEntity
                .created(buildHeaderLocation(workspaceResponse.getId()))
                .body(workspaceResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<WorkspaceResponse> update(
            @PathVariable String id,
            @Valid @RequestBody WorkspaceRequest request
    ){
        return ResponseEntity
                .ok(contract.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ){
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }
}
