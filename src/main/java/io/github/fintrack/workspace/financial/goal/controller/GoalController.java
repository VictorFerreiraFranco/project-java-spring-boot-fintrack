package io.github.fintrack.workspace.financial.goal.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.workspace.financial.goal.controller.contract.GoalContract;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalFilter;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalRequest;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finances/goals")
@RequiredArgsConstructor()
public class GoalController implements GenericController {

    private final GoalContract contract;

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getById(
            @ValidUUID @PathVariable String id
    ) {
        return ResponseEntity.ok(contract.getById(id));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<GoalResponse>> searchAllByWorkspace(
            @ValidUUID @PathVariable String workspaceId,
            @RequestBody GoalFilter filter
    ) {
        return ResponseEntity.ok(contract.searchAllByWorkspace(workspaceId, filter));
    }

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<GoalResponse> register(
            @ValidUUID @PathVariable String categoryId,
            @RequestBody GoalRequest request
    ) {
        GoalResponse goalResponse = contract.register(categoryId, request);
        return ResponseEntity
                .created(buildHeaderLocation(goalResponse.id()))
                .body(goalResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GoalResponse> update(
            @ValidUUID @PathVariable String id,
            @RequestBody GoalRequest request
    ) {
        return ResponseEntity.ok(contract.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @ValidUUID @PathVariable String id
    ) {
        this.contract.delete(id);
        return ResponseEntity.noContent().build();
    }

}
