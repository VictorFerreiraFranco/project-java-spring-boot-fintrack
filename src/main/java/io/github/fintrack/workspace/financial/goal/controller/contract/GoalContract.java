package io.github.fintrack.workspace.financial.goal.controller.contract;

import io.github.fintrack.workspace.financial.category.exception.CategoryNotFoundException;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.service.CategoryService;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalFilter;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalRequest;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalResponse;
import io.github.fintrack.workspace.financial.goal.controller.mapper.GoalMapper;
import io.github.fintrack.workspace.financial.goal.exception.GoalNotFoundException;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import io.github.fintrack.workspace.financial.goal.service.GoalService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GoalContract {

    private final GoalService goalService;
    private final CategoryService categoryService;
    private final WorkspaceService workspaceService;
    private final GoalMapper goalMapper;
    private final WorkspaceValidator workspaceValidator;

    @Transactional(readOnly = true)
    public GoalResponse getById(String id) {
        return goalMapper.toResponse(
                this.findById(id)
        );
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> searchAllByWorkspace(String workspaceId, GoalFilter filter) {
        return goalService.searchAllByWorkspace(this.findWorkspaceById(workspaceId), filter)
                .stream()
                .map(goalMapper::toResponse)
                .toList();
    }

    @Transactional()
    public GoalResponse register(String categoryId, GoalRequest request) {
        Goal goal = goalMapper.toEntity(request);
        goal.setCategory(this.findCategoryById(categoryId));
        return goalMapper.toResponse(
                goalService.save(goal)
        );
    }

    @Transactional()
    public GoalResponse update(String id, GoalRequest request) {
        Goal goal = this.findById(id);
        goalMapper.updateEntity(goal, request);
        return goalMapper.toResponse(
                goalService.save(goal)
        );
    }

    @Transactional()
    public void delete(String id) {
        goalService.delete(
                this.findById(id)
        );
    }

    private Goal findById(String id) {
        Goal goal = goalService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .orElseThrow(GoalNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(goal.getCategory().getWorkspace());

        return goal;
    }

    private Category findCategoryById(String categoryId) {
        Category category = categoryService.findByIdAndDeletedAtIsNull(UUID.fromString(categoryId))
                .orElseThrow(CategoryNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(category.getWorkspace());

        return category;
    }

    private Workspace findWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(workspace);

        return workspace;
    }
}
