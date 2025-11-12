package io.github.fintrack.workspace.financial.category.controller.contract;

import io.github.fintrack.workspace.financial.category.controller.dto.CategoryRegisterRequest;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryResponse;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryUpdateRequest;
import io.github.fintrack.workspace.financial.category.controller.mapper.CategoryMapper;
import io.github.fintrack.workspace.financial.category.exception.CategoryNotFoundException;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.service.CategoryService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryContract {

    private final CategoryService categoryService;
    private final WorkspaceService workspaceService;
    private final CategoryMapper categoryMapper;
    private final WorkspaceValidator workspaceValidator;

    public CategoryResponse getById(String id) {
        return categoryMapper.toDto(
            this.findById(id)
        );
    }

    public List<CategoryResponse> getAllByWorkspace(String workspaceId) {
        Workspace workspace = this.findWorkspaceById(workspaceId);

        workspaceValidator.userLoggedInIsNotMemberByWorkspace(workspace);

        return categoryService.findAllByWorkspaceAndDeletedAtIsNull(workspace)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public CategoryResponse register(CategoryRegisterRequest request) {
        Category entity = categoryMapper.toEntity(request);
        entity.setWorkspace(findWorkspaceById(request.workspaceId));

        return categoryMapper.toDto(categoryService.save(entity));
    }

    public CategoryResponse update(String id, CategoryUpdateRequest request) {
        Category category = this.findById(id);
        categoryMapper.updateEntity(category, request);

        return categoryMapper.toDto(
                categoryService.save(category)
        );
    }

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
        return workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);
    }
}
