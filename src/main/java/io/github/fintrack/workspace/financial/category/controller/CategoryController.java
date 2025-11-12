package io.github.fintrack.workspace.financial.category.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.workspace.financial.category.controller.contract.CategoryContract;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryRegisterRequest;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryResponse;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<CategoryResponse>> getAllByWorkspace(
            @ValidUUID @PathVariable("workspaceId") String workspaceId
    ) {
        return ResponseEntity.ok(contract.getAllByWorkspace(workspaceId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> register(
            @Valid @RequestBody CategoryRegisterRequest request
    ) {
        CategoryResponse categoryResponse = contract.register(request);
        return ResponseEntity
                .created(this.buildHeaderLocation(categoryResponse.id()))
                .body(categoryResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @ValidUUID @PathVariable("id") String id,
            @Valid @RequestBody CategoryUpdateRequest request
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
