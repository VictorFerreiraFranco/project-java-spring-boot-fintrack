package io.github.fintrack.workspace.financial.category.controller.contract;

import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.controller.dto.*;
import io.github.fintrack.workspace.financial.category.controller.mapper.CategoryMapper;
import io.github.fintrack.workspace.financial.category.exception.CategoryNotFoundException;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.service.CategoryService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryContract {

    private final CategoryService categoryService;
    private final WorkspaceService workspaceService;
    private final CategoryMapper categoryMapper;
    private final WorkspaceValidator workspaceValidator;

    @Transactional(readOnly = true)
    public CategoryResponse getById(String id) {
        return categoryMapper.toResponse(
            this.findById(id)
        );
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> searchAllByWorkspace(String workspaceId, CategoryFilter filter) {
        return categoryService.searchAllByWorkspace(this.findWorkspaceById(workspaceId), filter)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public List<CategoryTypeResponse> getTypes() {
        return Arrays.stream(Type.values())
                .map(type -> new CategoryTypeResponse(type, type.getDescription()))
                .toList();
    }

    @Transactional()
    public CategoryResponse register(String workspaceId, CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        category.setWorkspace(this.findWorkspaceById(workspaceId));
        return categoryMapper.toResponse(
                categoryService.save(category)
        );
    }

    @Transactional()
    public CategoryResponse update(String id, CategoryRequest request) {
        Category category = this.findById(id);
        categoryMapper.updateEntity(category, request);
        return categoryMapper.toResponse(
                categoryService.save(category)
        );
    }

    @Transactional()
    public void delete(String id) {
        categoryService.delete(
                this.findById(id)
        );
    }

    private Category findById(String id) {
        Category category = categoryService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .orElseThrow(CategoryNotFoundException::new);

        workspaceValidator.userLoggedInIsNotMemberByWorkspace(category.getWorkspace());

        return category;
    }

    private Workspace findWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);

        workspaceValidator.userLoggedInIsNotMemberByWorkspace(workspace);

        return workspace;
    }
}
