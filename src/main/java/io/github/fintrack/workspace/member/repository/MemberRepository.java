package io.github.fintrack.workspace.member.repository;

import io.github.fintrack.workspace.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
}
