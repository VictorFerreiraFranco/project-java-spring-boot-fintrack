package io.github.fintrack.workspace.workspace.repository.specification;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.workspace.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class WorkspaceSpecificationIntegrationTest {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("deletedAt is null should return only active workspaces")
    void deletedAtIsNullShouldReturnOnlyActiveWorkspaces() {
        User user = this.createUser("Test");
        userRepository.save(user);

        Workspace workspaceActive = this.createWorkspace(user, "Test Workspace Active");

        Workspace workspaceDeleted = this.createWorkspace(user, "Test Workspace Deleted");
        workspaceDeleted.getDeletion().markAsDeleted(user);

        workspaceRepository.saveAll(List.of(workspaceActive, workspaceDeleted));

        var result = workspaceRepository.findAll(WorkspaceSpecification.deletedAtIsNull());

        assertThat(result)
                .hasSize(1)
                .contains(workspaceActive)
                .doesNotContain(workspaceDeleted);
    }

    @Test
    @DisplayName("members.user equal should filter by user")
    void membersUserEqualShouldFilterByUser()
    {
        User user1 = this.createUser("Test1");
        User user2 = this.createUser("Test2");
        userRepository.saveAll(List.of(user1, user2));

        Workspace workspaceByUser1 = this.createWorkspace(user1, "Test Workspace 1");
        Workspace workspaceByUser2 = this.createWorkspace(user2, "Test Workspace 2");
        workspaceRepository.saveAll(List.of(workspaceByUser1, workspaceByUser2));

        Member memberByUser1 = this.createMember(user1, workspaceByUser1);
        Member memberByUser2 = this.createMember(user2, workspaceByUser2);
        memberRepository.saveAll(List.of(memberByUser1, memberByUser2));

        var result = workspaceRepository.findAll(WorkspaceSpecification.membersUserEqual(user1));

        assertThat(result)
                .hasSize(1)
                .contains(workspaceByUser1)
                .doesNotContain(workspaceByUser2);
    }

    private Member createMember(User user, Workspace workspace) {
        Member member = Member.builder()
                .user(user)
                .workspace(workspace)
                .role(io.github.fintrack.workspace.member.model.Role.OWNER)
                .build();

        member.getCreation().setCreatedBy(user);
        member.getCreation().setCreatedAt(LocalDateTime.now());

        return member;
    }

    private User createUser(String name) {
        return User.builder()
                .name(name)
                .email(name + "@email.com")
                .role(Role.USER)
                .password("123456")
                .build();
    }

    private Workspace createWorkspace(User user, String name) {
        Workspace workspace = Workspace.builder()
                .name(name)
                .type(Type.MAIN)
                .build();

        workspace.getCreation().setCreatedBy(user);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());

        return workspace;
    }
}
