package io.github.fintrack.workspace.workspace.repository;


import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.workspace.workspace.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class WorkspaceRepositoryTest {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Test User")
                .email("test@email.com")
                .role(Role.USER)
                .password("123456")
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("Should findById and deletionDeletedAtIsNull")
    void shouldFindByIdAndDeletionDeletedAtIsNull() {

        Workspace workspace = this.createWorkspace(user);

        workspaceRepository.save(workspace);

        Optional<Workspace> result = workspaceRepository.findByIdAndDeletion_DeletedAtIsNull(workspace.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(workspace.getId());
    }

    @Test
    @DisplayName("Should findByIdAndDeletionDeletedAtIsNull")
    void shouldIfNotExistfindByIdAndDeletionDeletedAtIsNull() {
        Assertions.assertThat(workspaceRepository.findAll()).isEmpty();

        Workspace workspace = this.createWorkspace(user);

        workspace.getDeletion()
                .markAsDeleted(user);

        workspaceRepository.save(workspace);

        Assertions.assertThat(workspaceRepository.findAll()).isNotEmpty();
        Assertions.assertThat(workspaceRepository.findByIdAndDeletion_DeletedAtIsNull(workspace.getId())).isNotPresent();
    }

    private Workspace createWorkspace(User user) {
        Workspace workspace = Workspace.builder()
                .name("Test Workspace")
                .type(Type.MAIN)
                .build();

        workspace.getCreation().setCreatedBy(user);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());

        return workspace;
    }
}
