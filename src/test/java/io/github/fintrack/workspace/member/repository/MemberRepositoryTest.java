package io.github.fintrack.workspace.member.repository;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

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
        user.setEmail("text@gmail.com");
        user.setPassword("123456");
        user.setRole(Role.USER);

        userRepository.save(user);

        workspace = new Workspace();
        workspace.setName("Test Workspace");
        workspace.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
        workspace.getCreation().setCreatedBy(user);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());

        workspaceRepository.save(workspace);

        member = new Member();
        member.setUser(user);
        member.setWorkspace(workspace);
        member.setRole(io.github.fintrack.workspace.member.model.Role.OWNER);
        member.getCreation().setCreatedBy(user);
        member.getCreation().setCreatedAt(LocalDateTime.now());

        memberRepository.save(member);
    }

    @Test
    @DisplayName("Should find by id and deletedAt is null")
    void shouldFindByIdWhenNotDeleted() {
        var result = memberRepository.findByIdAndDeletion_DeletedAtIsNull(member.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("Should not find by id and deletedAt is Not Null")
    void shouldNotFindByIdWhenDeletedAtIsNotNull() {
        member.getDeletion().markAsDeleted(user);
        memberRepository.save(member);

        var result = memberRepository.findByIdAndDeletion_DeletedAtIsNull(member.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find by workspace and user when deletedAt is null")
    void shouldFindByWorkspaceAndUserWhenNotDeleted() {
        var result = memberRepository.findByWorkspaceAndUserAndDeletion_DeletedAtIsNull(workspace, user);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(member.getId());
        assertThat(result.get().getWorkspace().getId()).isEqualTo(workspace.getId());
        assertThat(result.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Should not find by workspace and user when deletedAt is not null")
    void shouldNotFindByWorkspaceAndUserWhenDeletedAtIsNotNull() {
        member.getDeletion().markAsDeleted(user);
        memberRepository.save(member);

        var result = memberRepository.findByWorkspaceAndUserAndDeletion_DeletedAtIsNull(workspace, user);

        assertThat(result).isEmpty();
    }
}
