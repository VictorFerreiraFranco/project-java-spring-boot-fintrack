package io.github.fintrack.workspace.invite.repository;

import io.github.fintrack.workspace.invite.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
}
