package io.github.fintrack.workspace.financial.goal.repository;

import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID>, JpaSpecificationExecutor<Goal> {
    Optional<Goal> findByIdAndDeletionDeletedAtIsNull(UUID id);

    Optional<Goal> findByCategoryAndDeletionDeletedAtIsNull(Category category);
}
