package io.github.fintrack.workspace.financial.goal.repository.specification;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalFilter;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import io.github.fintrack.workspace.financial.goal.repository.GoalRepository;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class GoalSpecificationIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GoalRepository goalRepository;

    private User user;
    private Workspace workspace;
    private Category category;
    private Goal goal;

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
        category.setDescription("Food");
        category.setColor("#FFFFFF");
        category.setWorkspace(workspace);
        category.setType(Type.EXPENSE);
        category.getCreation().setCreatedBy(user);
        category.getCreation().setCreatedAt(LocalDateTime.now());
        categoryRepository.save(category);

        goal = new Goal();
        goal.setCategory(category);
        goal.setDescription("Buy groceries");
        goal.setAmount(BigDecimal.valueOf(500));
        goal.getCreation().setCreatedBy(user);
        goal.getCreation().setCreatedAt(LocalDateTime.now());
        goalRepository.save(goal);
    }

    @Test
    @DisplayName("Should filter goals where deletedAt is null")
    void shouldFilterDeletedAtIsNull() {
        Specification<Goal> spec = GoalSpecification.deletedAtIsNull();
        var result = goalRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(goal.getId());
    }

    @Test
    @DisplayName("Should return empty list when deletedAt is not null")
    void shouldReturnEmptyWhenDeleted() {
        goal.getDeletion().markAsDeleted(user);
        goalRepository.save(goal);

        Specification<Goal> spec = GoalSpecification.deletedAtIsNull();
        var result = goalRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should filter by description using LIKE case-insensitive")
    void shouldFilterByDescriptionLike() {
        Specification<Goal> spec = GoalSpecification.descriptionLike("gro");

        var result = goalRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getDescription()).isEqualTo("Buy groceries");
    }

    @Test
    @DisplayName("Should not match description when text differs")
    void shouldNotMatchDescriptionLike() {
        Specification<Goal> spec = GoalSpecification.descriptionLike("travel");

        var result = goalRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should filter by amount")
    void shouldFilterByAmountEquals() {
        Specification<Goal> spec = GoalSpecification.amountEquals(BigDecimal.valueOf(500));

        var result = goalRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getAmount()).isEqualTo(BigDecimal.valueOf(500));
    }

    @Test
    @DisplayName("Should not match when amount differs")
    void shouldNotMatchAmountEquals() {
        Specification<Goal> spec = GoalSpecification.amountEquals(BigDecimal.TEN);

        var result = goalRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should filter by category ID")
    void shouldFilterByCategoryId() {
        Specification<Goal> spec = GoalSpecification.categoryEqual(category.getId());

        var result = goalRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("Should not match when category ID differs")
    void shouldNotMatchCategoryId() {
        Specification<Goal> spec = GoalSpecification.categoryEqual(UUID.randomUUID());

        var result = goalRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should filter by workspace through category")
    void shouldFilterByWorkspace() {
        Specification<Goal> spec = GoalSpecification.workspaceEqual(workspace);

        var result = goalRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCategory().getWorkspace()).isEqualTo(workspace);
    }

    @Test
    @DisplayName("Should not match when workspace differs")
    void shouldNotMatchWorkspace() {
        Workspace other = new Workspace();
        other.setName("Other");
        other.setType(io.github.fintrack.workspace.workspace.model.Type.MAIN);
        other.getCreation().setCreatedBy(user);
        other.getCreation().setCreatedAt(LocalDateTime.now());
        workspaceRepository.save(other);

        Specification<Goal> spec = GoalSpecification.workspaceEqual(other);

        var result = goalRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("applyFilter should combine description, amount and category")
    void applyFilterShouldCombine() {
        var filter = new GoalFilter(
                "gro",
                BigDecimal.valueOf(500),
                category.getId().toString()
        );

        Specification<Goal> spec = GoalSpecification.applyFilter(filter);

        var result = goalRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(goal.getId());
    }

    @Test
    @DisplayName("applyFilter should return empty when any field does not match")
    void applyFilterShouldReturnEmpty() {
        var filter = new GoalFilter(
                "gro",
                BigDecimal.valueOf(999),
                category.getId().toString()
        );

        Specification<Goal> spec = GoalSpecification.applyFilter(filter);

        var result = goalRepository.findAll(spec);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("applyFilter should return all when filter is null")
    void applyFilterNullShouldReturnAll() {
        Specification<Goal> spec = GoalSpecification.applyFilter(null);

        var result = goalRepository.findAll(spec);

        assertThat(result).hasSize(1);
    }
}
