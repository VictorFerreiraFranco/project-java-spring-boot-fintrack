package io.github.fintrack.workspace.financial.category.repository;

import io.github.fintrack.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByIdAndDeletion_DeletedAtIsNull(UUID id);

//    List<Category> findAllByWorkspaceAndDeletion_DeletedAtIsNull(Workspace workspace);

    Optional<Category> findByWorkspaceAndDescriptionIgnoreCaseAndTypeAndDeletion_DeletedAtIsNull(Workspace workspace, String description, Type type);
}
