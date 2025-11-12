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

    public CategoryResponse getById(String id) {
        return categoryMapper.toDto(
            this.finById(id)
        );
    }

    public List<CategoryResponse> getAllByWorkspace(String workspaceId) {
        return categoryService.findAllByWorkspaceAndDeletedAtIsNull(this.findWorkspaceById(workspaceId))
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
        Category category = this.finById(id);
        categoryMapper.updateEntity(category, request);

        return categoryMapper.toDto(
                categoryService.save(category)
        );
    }

    public void delete(String id) {
        categoryService.delete(
                categoryService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                        .orElseThrow(CategoryNotFoundException::new)
        );
    }

    private Category finById(String id) {
        return  categoryService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .orElseThrow(CategoryNotFoundException::new);
    }

    private Workspace findWorkspaceById(String workspaceId) {
        return workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);
    }
}
