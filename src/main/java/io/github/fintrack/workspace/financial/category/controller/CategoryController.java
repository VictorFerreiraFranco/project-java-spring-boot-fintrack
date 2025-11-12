package io.github.fintrack.workspace.financial.category.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.workspace.financial.category.controller.contract.CategoryContract;
import io.github.fintrack.workspace.financial.category.controller.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController implements GenericController {

    private final CategoryContract contract;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(
            @ValidUUID @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(contract.getById(id));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<CategoryResponse>> searchAllByWorkspace(
            @ValidUUID @PathVariable("workspaceId") String workspaceId,
            @RequestBody CategoryFilter filter
            ) {
        return ResponseEntity.ok(contract.searchAllByWorkspace(workspaceId, filter));
    }

    @GetMapping("/types")
    public ResponseEntity<List<CategoryTypeResponse>> getById() {
        return ResponseEntity.ok(contract.getTypes());
    }

    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<CategoryResponse> register(
            @ValidUUID @PathVariable("workspaceId") String workspaceId,
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse categoryResponse = contract.register(workspaceId, request);
        return ResponseEntity
                .created(this.buildHeaderLocation(categoryResponse.id()))
                .body(categoryResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @ValidUUID @PathVariable("id") String id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(contract.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @ValidUUID @PathVariable("id") String id
    ) {
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }

}
