package io.github.fintrack.workspace.invite.repository.specification;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.invite.repository.InviteRepository;
import io.github.fintrack.workspace.member.model.Member;
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
public class InviteSpecificationIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InviteRepository inviteRepository;

    private User userTo;
    private User userFrom;
    private Workspace workspace;
    private Invite invite;

    @BeforeEach
    void setUp() {
        userTo = new User();
        userTo.setName("Test User");
        userTo.setEmail("text@gmail.com");
        userTo.setPassword("123456");
        userTo.setRole(Role.USER);

        userFrom = new User();
        userFrom.setName("Test User 2");
        userFrom.setEmail("text2@gmail.com");
        userFrom.setPassword("123456");
        userFrom.setRole(Role.USER);

        userRepository.saveAll(List.of(userTo, userFrom));

        workspace = new Workspace();
        workspace.setName("Test Workspace");
        workspace.setType(Type.MAIN);
        workspace.getCreation().setCreatedBy(userTo);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());
        workspaceRepository.save(workspace);

        Member member = new Member();
        member.setUser(userTo);
        member.setWorkspace(workspace);
        member.setRole(io.github.fintrack.workspace.member.model.Role.OWNER);
        member.getCreation().setCreatedBy(userTo);
        member.getCreation().setCreatedAt(LocalDateTime.now());
        memberRepository.save(member);

        invite = new Invite();
        invite.setStatus(Status.PENDING);
        invite.setWorkspace(workspace);
        invite.setTo(userTo);
        invite.setFrom(userFrom);
        invite.getCreation().setCreatedBy(userTo);
        invite.getCreation().setCreatedAt(LocalDateTime.now());
        inviteRepository.save(invite);
    }

    @Test
    @DisplayName("workspaceEqual: Should filter invites by workspace")
    void shouldFilterByWorkspace() {
        Specification<Invite> spec = InviteSpecification.workspaceEqual(workspace);

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getWorkspace().getId()).isEqualTo(workspace.getId());
    }

    @Test
    @DisplayName("workspaceEqual: Should return empty when workspace does not match")
    void shouldReturnEmptyWhenWorkspaceDoesNotMatch() {
        Workspace other = new Workspace();
        other.setName("Other WS");
        other.setType(Type.MAIN);
        other.getCreation().setCreatedBy(userTo);
        other.getCreation().setCreatedAt(LocalDateTime.now());
        workspaceRepository.save(other);

        Specification<Invite> spec = InviteSpecification.workspaceEqual(other);

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("toEqual: Should filter invites by 'to' user")
    void shouldFilterByToUser() {
        Specification<Invite> spec = InviteSpecification.toEqual(userTo);

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTo().getId()).isEqualTo(userTo.getId());
    }

    @Test
    @DisplayName("toEqual: Should return empty when 'to' does not match")
    void shouldReturnEmptyWhenToDoesNotMatch() {
        Specification<Invite> spec = InviteSpecification.toEqual(userFrom);

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("statusEqual: Should filter invites by status")
    void shouldFilterByStatus() {
        Specification<Invite> spec = InviteSpecification.statusEqual(Status.PENDING);

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    @DisplayName("statusEqual: Should return empty when status does not match")
    void shouldReturnEmptyWhenStatusDoesNotMatch() {
        Specification<Invite> spec = InviteSpecification.statusEqual(Status.ACCEPTED);

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Combined: workspaceEqual AND toEqual AND statusEqual should return invite")
    void shouldCombineSpecifications() {
        Specification<Invite> spec =
                InviteSpecification.workspaceEqual(workspace)
                        .and(InviteSpecification.toEqual(userTo))
                        .and(InviteSpecification.statusEqual(Status.PENDING));

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(invite.getId());
    }

    @Test
    @DisplayName("Combined: Should return empty when one condition fails")
    void shouldReturnEmptyWhenAnyConditionFails() {
        invite.setStatus(Status.ACCEPTED);
        inviteRepository.save(invite);

        Specification<Invite> spec =
                InviteSpecification.workspaceEqual(workspace)
                        .and(InviteSpecification.toEqual(userTo))
                        .and(InviteSpecification.statusEqual(Status.PENDING)); // agora não corresponde

        List<Invite> result = inviteRepository.findAll(spec);

        assertThat(result).isEmpty();
    }
}
