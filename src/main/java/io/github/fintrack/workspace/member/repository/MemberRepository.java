package io.github.fintrack.workspace.member.repository;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID>, JpaSpecificationExecutor<Member> {
    Optional<Member> findByIdAndDeletionDeletedAtIsNull(UUID id);

    Optional<Member> findByWorkspaceAndUserAndDeletionDeletedAtIsNull(Workspace workspace, User user);
}
