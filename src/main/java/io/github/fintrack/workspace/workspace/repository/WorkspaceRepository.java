package io.github.fintrack.workspace.workspace.repository;

import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID>, JpaSpecificationExecutor<Workspace> {
    Optional<Workspace> findByIdAndDeletionDeletedAtIsNull(UUID uuid);
}
