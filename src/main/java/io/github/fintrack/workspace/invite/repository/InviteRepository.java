package io.github.fintrack.workspace.invite.repository;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID>, JpaSpecificationExecutor<Invite> {
    Optional<Invite> findByToAndWorkspaceAndStatus(User to, Workspace workspace, Status status);
}
