package io.github.fintrack.workspace.financial.category.repository;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.model.Category;
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
public class CategoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Workspace workspace;
    private Category category;

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

        category = new Category();
        category.setDescription("Test Category");
        category.setColor("#FFFFFF");
        category.setWorkspace(workspace);
        category.setType(Type.EXPENSE);
        category.getCreation().setCreatedBy(user);
        category.getCreation().setCreatedAt(LocalDateTime.now());

        categoryRepository.save(category);
    }


    @Test
    @DisplayName("Should find by id and deleted At Is Null")
    void shouldFindByIdWhenNotDeleted() {
        var result = categoryRepository.findByIdAndDeletionDeletedAtIsNull(category.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo(category.getDescription());
    }

    @Test
    @DisplayName("Should not find by id and deletedAt Is Not Null")
    void shouldNotFindByIdWhenDeletedAtIsNotNull() {
        category.getDeletion().markAsDeleted(user);
        categoryRepository.save(category);

        var result = categoryRepository.findByIdAndDeletionDeletedAtIsNull(category.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find by workspace and description ignore case and type deletedAt is null")
    void shouldFindByWorkspaceAndDescriptionIgnoreCaseAndTypeWhenDeletedAtIsNull() {
        var result = categoryRepository.findByWorkspaceAndDescriptionIgnoreCaseAndTypeAndDeletionDeletedAtIsNull(
                workspace,
                category.getDescription().toLowerCase(),
                category.getType()
        );

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("Should find by workspace and description ignore case and type when deletedAt is not null")
    void shouldFindByWorkspaceAndDescriptionIgnoreCaseAndTypeWhenDeletedAtIsNotNull() {
        category.getDeletion().markAsDeleted(user);
        category.setDescription("Test Category Updated");
        categoryRepository.save(category);

        var result = categoryRepository.findByWorkspaceAndDescriptionIgnoreCaseAndTypeAndDeletionDeletedAtIsNull(
                workspace,
                category.getDescription().toLowerCase(),
                category.getType()
        );

        assertThat(result).isNotPresent();
    }
}
