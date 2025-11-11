package io.github.fintrack.workspace.invite.repository;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    List<Invite> findAllByWorkspaceAndStatus(Workspace workspace, Status status);

    List<Invite> findAllByStatusAndTo(Status status, User to);

    Optional<Invite> findByToAndWorkspaceAndStatus(User to, Workspace workspace, Status status);
}
