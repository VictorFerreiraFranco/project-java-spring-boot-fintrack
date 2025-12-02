package io.github.fintrack.workspace.financial.category.repository;

import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    Optional<Category> findByIdAndDeletionDeletedAtIsNull(UUID id);

    Optional<Category> findByWorkspaceAndDescriptionIgnoreCaseAndTypeAndDeletionDeletedAtIsNull(Workspace workspace, String description, Type type);
}
