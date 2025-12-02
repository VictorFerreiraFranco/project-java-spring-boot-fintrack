package io.github.fintrack.workspace.member.repository.specification;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.workspace.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberSpecificationIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private MemberRepository memberRepository;

    private User user;
    private Workspace workspace;
    private Member member;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setPassword("123456");
        user.setRole(io.github.fintrack.auth.model.Role.USER);
        userRepository.save(user);

        workspace = new Workspace();
        workspace.setName("Workspace Test");
        workspace.setType(Type.MAIN);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());
        workspace.getCreation().setCreatedBy(user);
        workspaceRepository.save(workspace);

        member = new Member();
        member.setUser(user);
        member.setWorkspace(workspace);
        member.setRole(Role.OWNER);
        member.getCreation().setCreatedAt(LocalDateTime.now());
        member.getCreation().setCreatedBy(user);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("workspaceEqual: Should filter members by workspace")
    void shouldFilterByWorkspace() {
        Specification<Member> spec = MemberSpecification.workspaceEqual(workspace);

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getWorkspace().getId()).isEqualTo(workspace.getId());
    }

    @Test
    @DisplayName("workspaceEqual: Should return empty when workspace does not match")
    void shouldReturnEmptyWhenWorkspaceDoesNotMatch() {
        Workspace other = new Workspace();
        other.setName("Other WS");
        other.setType(Type.MAIN);
        other.getCreation().setCreatedAt(LocalDateTime.now());
        other.getCreation().setCreatedBy(user);
        workspaceRepository.save(other);

        Specification<Member> spec = MemberSpecification.workspaceEqual(other);

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("roleEqual: Should filter members by role")
    void shouldFilterByRole() {
        Specification<Member> spec = MemberSpecification.roleEqual(Role.OWNER);

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getRole()).isEqualTo(Role.OWNER);
    }

    @Test
    @DisplayName("roleEqual: Should return empty for role that does not match")
    void shouldReturnEmptyForDifferentRole() {
        Specification<Member> spec = MemberSpecification.roleEqual(Role.MEMBER);

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deletedAtIsNull: Should return only active members")
    void shouldFilterDeletedAtIsNull() {
        Specification<Member> spec = MemberSpecification.deletedAtIsNull();

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getDeletion().getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("deletedAtIsNull: Should exclude deleted members")
    void shouldExcludeDeletedMembers() {
        member.getDeletion().markAsDeleted(user);
        memberRepository.save(member);

        Specification<Member> spec = MemberSpecification.deletedAtIsNull();

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("Combined: workspaceEqual AND roleEqual AND deletedAtIsNull should return only valid member")
    void shouldCombineSpecifications() {
        Specification<Member> spec =
                MemberSpecification.workspaceEqual(workspace)
                        .and(MemberSpecification.roleEqual(Role.OWNER))
                        .and(MemberSpecification.deletedAtIsNull());

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("Combined: Should return empty when one spec fails")
    void shouldReturnEmptyWhenOneConditionFails() {
        member.getDeletion().markAsDeleted(user);
        memberRepository.save(member);

        Specification<Member> spec =
                MemberSpecification.workspaceEqual(workspace)
                        .and(MemberSpecification.roleEqual(Role.OWNER))
                        .and(MemberSpecification.deletedAtIsNull());

        List<Member> result = memberRepository.findAll(spec);

        assertThat(result).isEmpty();
    }
}
