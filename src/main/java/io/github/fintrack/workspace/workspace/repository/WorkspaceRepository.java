package io.github.fintrack.workspace.workspace.repository;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    Optional<Workspace> findByIdAndDeletion_DeletedAtIsNull(UUID uuid);

    List<Workspace> findAllByMembers_UserAndDeletion_DeletedAtIsNull(User user);
}
