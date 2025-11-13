package io.github.fintrack.workspace.financial.goal.validator;

import io.github.fintrack.common.exception.DuplicateRecordException;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import io.github.fintrack.workspace.financial.goal.repository.GoalRepository;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GoalValidator {

    private final GoalRepository goalRepository;
    private final WorkspaceValidator workspaceValidator;

    public void validToSave(Goal goal) {
        if (goalExist(goal))
            throw new DuplicateRecordException("Goal already exists");

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(goal.getCategory().getWorkspace());
    }

    public void validToDelete(Goal goal) {
        workspaceValidator.validUserLoggedInIsMemberByWorkspace(goal.getCategory().getWorkspace());
    }

    public boolean goalExist(Goal goal) {
        Optional<Goal> goalFund = goalRepository.findByCategory(goal.getCategory());

        if (goal.getId() == null)
            return goalFund.isPresent();

        return goalFund.isPresent() && !goalFund.get().getId().equals(goal.getId());
    }
}
