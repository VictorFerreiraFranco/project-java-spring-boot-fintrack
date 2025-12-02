package io.github.fintrack.workspace.payment.method.repository;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.repository.MemberRepository;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.model.Type;
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
class MethodRepositoryTest {

    @Autowired
    private MethodRepository methodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private MemberRepository memberRepository;

    private User user;
    private Method method;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("text@gmail.com");
        user.setPassword("123456");
        user.setRole(Role.USER);

        userRepository.save(user);

        Workspace workspace = new Workspace();
        workspace.setName("Test Workspace");
        workspace.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
        workspace.getCreation().setCreatedBy(user);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());

        workspaceRepository.save(workspace);

        Member member = new Member();
        member.setUser(user);
        member.setWorkspace(workspace);
        member.setRole(io.github.fintrack.workspace.member.model.Role.OWNER);
        member.getCreation().setCreatedBy(user);
        member.getCreation().setCreatedAt(LocalDateTime.now());

        memberRepository.save(member);

        method = new Method();
        method.setDescription("Test");
        method.setType(Type.BANK_TRANSFER);
        method.setWorkspace(workspace);
        method.getCreation().setCreatedBy(user);
        method.getCreation().setCreatedAt(LocalDateTime.now());

        methodRepository.save(method);
    }

    @Test
    @DisplayName("Should find by id and deleted At Is Null")
    void shouldFindByIdWhenNotDeleted() {
        var result = methodRepository.findByIdAndDeletionDeletedAtIsNull(method.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo(method.getDescription());
    }

    @Test
    @DisplayName("Should not find by id and deletedAt Is Not Null")
    void shouldNotFindByIdWhenDeletedAtIsNotNull() {
        method.getDeletion().markAsDeleted(user);

        Method saved = methodRepository.save(method);

        var result = methodRepository.findByIdAndDeletionDeletedAtIsNull(saved.getId());

        assertThat(result).isEmpty();
    }
}
