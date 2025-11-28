package io.github.fintrack.workspace.invite.repository;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class InviteRepositoryTest {

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
        workspace.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
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
    @DisplayName("Should find by to and workspace and status pending")
    void shouldFindByToAndWorkspaceAndStatusPending() {
        var result = inviteRepository.findByToAndWorkspaceAndStatus(userTo, workspace, Status.PENDING);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(invite.getId());
    }

    @Test
    @DisplayName("Should find by to and workspace and status accepted")
    void shouldFindByToAndWorkspaceAndStatusAccepted() {
        invite.setStatus(Status.ACCEPTED);
        inviteRepository.save(invite);

        var result = inviteRepository.findByToAndWorkspaceAndStatus(userTo, workspace, Status.ACCEPTED);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(invite.getId());
    }

    @Test
    @DisplayName("Should find by to and workspace and status refused")
    void shouldFindByToAndWorkspaceAndStatusRefused() {
        invite.setStatus(Status.REFUSED);
        inviteRepository.save(invite);

        var result = inviteRepository.findByToAndWorkspaceAndStatus(userTo, workspace, Status.REFUSED);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(invite.getId());
    }

    @Test
    @DisplayName("Should find by to and workspace and status canceled")
    void shouldFindByToAndWorkspaceAndStatusCanceled() {
        invite.setStatus(Status.CANCELED);
        inviteRepository.save(invite);

        var result = inviteRepository.findByToAndWorkspaceAndStatus(userTo, workspace, Status.CANCELED);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(invite.getId());
    }

    @Test
    @DisplayName("Should not find by to and workspace and status when user is invalid")
    void shouldNotFindByToAndWorkspaceAndStatusWhenUserIsInvalid() {
        var result = inviteRepository.findByToAndWorkspaceAndStatus(userFrom, workspace, Status.PENDING);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should not find by to and workspace and status when workspace is invalid")
    void shouldNotFindByToAndWorkspaceAndStatusWhenWorkspaceIsInvalid() {
        Workspace invalidWorkspace = new Workspace();
        invalidWorkspace.setId(UUID.randomUUID());

        var result = inviteRepository.findByToAndWorkspaceAndStatus(userTo, invalidWorkspace, Status.PENDING);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should not find by to and workspace and status when status is invalid")
    void shouldNotFindByToAndWorkspaceAndStatusWhenStatusIsInvalid() {
        var result = inviteRepository.findByToAndWorkspaceAndStatus(userTo, workspace, Status.ACCEPTED);

        assertThat(result).isEmpty();
    }
}