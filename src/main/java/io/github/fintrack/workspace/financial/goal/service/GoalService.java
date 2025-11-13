package io.github.fintrack.workspace.financial.goal.service;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalFilter;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import io.github.fintrack.workspace.financial.goal.repository.GoalRepository;
import io.github.fintrack.workspace.financial.goal.repository.specification.GoalSpecification;
import io.github.fintrack.workspace.financial.goal.validator.GoalValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final AuthService authService;
    private final GoalValidator goalValidator;

    public Optional<Goal> findByIdAndDeletedAtIsNull(UUID id) {
        return goalRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public List<Goal> searchAllByWorkspace(
            Workspace workspace, GoalFilter filter
    ) {
        return goalRepository.findAll(
                GoalSpecification.deletedAtIsNull()
                        .and(GoalSpecification.workspaceEqual(workspace))
                        .and(GoalSpecification.applyFilter(filter))
        );
    }

    @Transactional
    public Goal save(Goal goal) {
        goalValidator.validToSave(goal);
        return goalRepository.save(goal);
    }

    @Transactional
    public void delete(Goal goal) {
        goalValidator.validToDelete(goal);
        goal.getDeletion().markAsDeleted(authService.getUserLoggedIn());
        goalRepository.save(goal);
    }
}
