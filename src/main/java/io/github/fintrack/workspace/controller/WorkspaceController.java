package io.github.fintrack.workspace.controller;

import io.github.fintrack.workspace.controller.contract.WorkspaceContract;
import io.github.fintrack.workspace.controller.dto.WorkspaceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceContract contract;

    @PostMapping
    public ResponseEntity<String> create(
            @Valid @RequestBody WorkspaceRequest request
    ){
        contract.create(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> update(
            @PathVariable String id,
            @Valid @RequestBody WorkspaceRequest request
    ){
        contract.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public  ResponseEntity<Void> delete(
            @PathVariable String id
    ){
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }

}
