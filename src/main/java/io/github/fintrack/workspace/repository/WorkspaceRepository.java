package io.github.fintrack.workspace.repository;

import io.github.fintrack.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
}
