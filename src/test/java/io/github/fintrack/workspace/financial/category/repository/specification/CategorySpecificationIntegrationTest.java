package io.github.fintrack.workspace.financial.category.repository.specification;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryFilter;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CategorySpecificationIntegrationTest {

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
        user.setEmail("test@gmail.com");
        user.setPassword("123456");
        user.setRole(Role.USER);
        userRepository.save(user);

        workspace = new Workspace();
        workspace.setName("Workspace Test");
        workspace.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
        workspace.getCreation().setCreatedAt(LocalDateTime.now());
        workspace.getCreation().setCreatedBy(user);
        workspaceRepository.save(workspace);

        category = new Category();
        category.setDescription("Food");
        category.setColor("#FFFFFF");
        category.setWorkspace(workspace);
        category.setType(Type.EXPENSE);
        category.getCreation().setCreatedBy(user);
        category.getCreation().setCreatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Test
    @DisplayName("workspaceEqual: Should filter by workspace")
    void shouldFilterByWorkspace() {
        Specification<Category> spec = CategorySpecification.workspaceEqual(workspace);

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getWorkspace().getId()).isEqualTo(workspace.getId());
    }

    @Test
    @DisplayName("workspaceEqual: Should return empty when workspace does not match")
    void shouldReturnEmptyWhenWorkspaceDoesNotMatch() {
        Workspace other = new Workspace();
        other.setName("Other");
        other.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
        other.getCreation().setCreatedBy(user);
        other.getCreation().setCreatedAt(LocalDateTime.now());
        workspaceRepository.save(other);

        Specification<Category> spec = CategorySpecification.workspaceEqual(other);

        var result = categoryRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deletedAtIsNull: Should return only not deleted categories")
    void shouldReturnOnlyNotDeleted() {
        Specification<Category> spec = CategorySpecification.deletedAtIsNull();

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("deletedAtIsNull: Should return empty when category is deleted")
    void shouldReturnEmptyWhenDeleted() {
        category.getDeletion().markAsDeleted(user);
        categoryRepository.save(category);

        Specification<Category> spec = CategorySpecification.deletedAtIsNull();

        var result = categoryRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("descriptionLike: Should match partial and case-insensitive")
    void shouldFilterByDescriptionLike() {
        Specification<Category> spec = CategorySpecification.descriptionLike("foo");

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("descriptionLike: Should return empty when no match")
    void shouldReturnEmptyForDescriptionLike() {
        Specification<Category> spec = CategorySpecification.descriptionLike("rent");

        var result = categoryRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("colorEqual: Should filter by color")
    void shouldFilterByColor() {
        Specification<Category> spec = CategorySpecification.colorEqual("#FFFFFF");

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("colorEqual: Should return empty when not match")
    void shouldReturnEmptyForColor() {
        Specification<Category> spec = CategorySpecification.colorEqual("#000000");

        var result = categoryRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("typeEqual: Should filter by type")
    void shouldFilterByType() {
        Specification<Category> spec = CategorySpecification.typeEqual(Type.EXPENSE);

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("typeEqual: Should return empty when type does not match")
    void shouldReturnEmptyForType() {
        Specification<Category> spec = CategorySpecification.typeEqual(Type.INCOME);

        var result = categoryRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("applyFilter: Should apply all filters and return result")
    void shouldApplyFilterWithAllFields() {
        CategoryFilter filter =
                new CategoryFilter("foo", "#FFFFFF", "EXPENSE");

        Specification<Category> spec = CategorySpecification.applyFilter(filter);

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("applyFilter: Should ignore empty fields")
    void shouldIgnoreEmptyFields() {
        CategoryFilter filter =
                new CategoryFilter("", "", "");

        Specification<Category> spec = CategorySpecification.applyFilter(filter);

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("applyFilter: Should return empty when any filter does not match")
    void shouldReturnEmptyWhenFilterDoesNotMatch() {
        CategoryFilter filter =
                new CategoryFilter("rent", "#FFFFFF", "EXPENSE");

        Specification<Category> spec = CategorySpecification.applyFilter(filter);

        var result = categoryRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("applyFilter: Should return all when filter is null")
    void shouldReturnAllWhenFilterIsNull() {
        Specification<Category> spec = CategorySpecification.applyFilter(null);

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Combined: workspace + description + type")
    void shouldCombineSpecifications() {
        Specification<Category> spec =
                CategorySpecification.workspaceEqual(workspace)
                        .and(CategorySpecification.descriptionLike("foo"))
                        .and(CategorySpecification.typeEqual(Type.EXPENSE));

        var result = categoryRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Combined: Should return empty when one condition fails")
    void shouldReturnEmptyWhenOneFails() {
        Specification<Category> spec =
                CategorySpecification.workspaceEqual(workspace)
                        .and(CategorySpecification.descriptionLike("invalid"))
                        .and(CategorySpecification.typeEqual(Type.EXPENSE));

        var result = categoryRepository.findAll(spec);

        assertThat(result).isEmpty();
    }
}
