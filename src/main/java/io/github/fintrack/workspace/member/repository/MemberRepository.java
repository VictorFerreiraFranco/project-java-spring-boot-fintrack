package io.github.fintrack.workspace.member.repository;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByIdAndDeletion_DeletedAtIsNull(UUID id);

    Optional<Member> findByWorkspaceAndUserAndDeletion_DeletedAtIsNull(Workspace workspace, User user);

    List<Member> findAllByWorkspaceAndRole(Workspace workspace, Role role);

    List<Member> findAllByWorkspaceAndDeletion_DeletedAtIsNull(Workspace workspace);
}
