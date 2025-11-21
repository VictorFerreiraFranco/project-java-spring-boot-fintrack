package io.github.fintrack.workspace.financial.goal.controller.contract;

import io.github.fintrack.workspace.financial.category.service.CategoryService;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalFilter;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalRequest;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalResponse;
import io.github.fintrack.workspace.financial.goal.controller.mapper.GoalMapper;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import io.github.fintrack.workspace.financial.goal.service.GoalService;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
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

    @Transactional(readOnly = true)
    public GoalResponse getById(String id) {
        return goalMapper.toResponse(
                goalService.findByIdAndValidateExistenceAndMembership(UUID.fromString(id))
        );
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> searchAllByWorkspace(String workspaceId, GoalFilter filter) {
        return goalService.searchAllByWorkspace(
                        workspaceService.findByIdAndValidateExistenceAndMembership(UUID.fromString(workspaceId)),
                        filter)
                .stream()
                .map(goalMapper::toResponse)
                .toList();
    }

    @Transactional
    public GoalResponse register(String categoryId, GoalRequest request) {
        Goal goal = goalMapper.toEntity(request);
        goal.setCategory(
                categoryService.findByIdAndValidateExistenceAndMembership(UUID.fromString(categoryId))
        );
        return goalMapper.toResponse(
                goalService.save(goal)
        );
    }

    @Transactional
    public GoalResponse update(String id, GoalRequest request) {
        Goal goal = goalService.findByIdAndValidateExistenceAndMembership(UUID.fromString(id));
        goalMapper.updateEntity(goal, request);
        return goalMapper.toResponse(
                goalService.save(goal)
        );
    }

    @Transactional
    public void delete(String id) {
        goalService.delete(
                this.goalService.findByIdAndValidateExistenceAndMembership(UUID.fromString(id))
        );
    }
}
