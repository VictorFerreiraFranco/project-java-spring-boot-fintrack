package io.github.fintrack.workspace.financial.goal.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.goal.controller.dto.GoalFilter;
import io.github.fintrack.workspace.financial.goal.exception.GoalNotFoundException;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import io.github.fintrack.workspace.financial.goal.repository.GoalRepository;
import io.github.fintrack.workspace.financial.goal.service.validator.GoalValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GoalServiceTest {

    private final GoalRepository goalRepository = mock(GoalRepository.class);
    private final AuthService authService = mock(AuthService.class);
    private final GoalValidator goalValidator = mock(GoalValidator.class);
    private final WorkspaceValidator workspaceValidator = mock(WorkspaceValidator.class);

    private final GoalService goalService = new GoalService(
            goalRepository, authService, goalValidator, workspaceValidator
    );

    @Test
    @DisplayName("Should find by id and deletedAt is null")
    void shouldFindByIdAndDeletedAtIsNull() {

        Goal goal = new Goal();
        goal.setId(UUID.randomUUID());

        when(goalRepository.findByIdAndDeletionDeletedAtIsNull(goal.getId()))
                .thenReturn(Optional.of(goal));

        Optional<Goal> result = goalService.findByIdAndDeletedAtIsNull(goal.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(goal.getId());

        verify(goalRepository).findByIdAndDeletionDeletedAtIsNull(goal.getId());
    }

    @Test
    @DisplayName("Should search all goals by workspace with filter")
    void shouldSearchAllByWorkspaceWithFilter() {
        Workspace workspace = new Workspace();
        GoalFilter filter = new GoalFilter("test", BigDecimal.TEN, UUID.randomUUID().toString());

        List<Goal> expected = List.of(new Goal(), new Goal());

        when(goalRepository.findAll(any(Specification.class)))
                .thenReturn(expected);

        List<Goal> result = goalService.searchAllByWorkspace(workspace, filter);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return goal when exists and user is member of workspace")
    void shouldReturnGoalWhenExistsAndUserIsMember() {
        UUID id = UUID.randomUUID();

        Workspace workspace = new Workspace();
        Category category = new Category();
        category.setWorkspace(workspace);

        Goal goal = new Goal();
        goal.setCategory(category);

        when(goalRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(goal));

        Goal result = goalService.findByIdAndValidateExistenceAndMembership(id);

        assertThat(result).isEqualTo(goal);
        verify(workspaceValidator).validUserLoggedInIsMemberByWorkspace(workspace);
    }

    @Test
    @DisplayName("Should throw exception when goal not found")
    void shouldThrowExceptionWhenGoalNotFound() {
        UUID id = UUID.randomUUID();

        when(goalRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                goalService.findByIdAndValidateExistenceAndMembership(id)
        ).isInstanceOf(GoalNotFoundException.class);
    }

    @Test
    @DisplayName("Should save goal")
    void shouldSaveGoal() {
        Goal goal = new Goal();
        when(goalRepository.save(goal)).thenReturn(goal);

        Goal result = goalService.save(goal);

        assertThat(result).isEqualTo(goal);
        verify(goalValidator).validToSave(goal);
        verify(goalRepository).save(goal);
    }

    @Test
    @DisplayName("Should delete goal")
    void shouldDeleteGoal() {
        User loggedUser = new User();

        Category category = new Category();
        category.setWorkspace(new Workspace());

        Goal goal = new Goal();
        goal.setCategory(category);

        when(authService.getUserLoggedIn()).thenReturn(loggedUser);
        when(goalRepository.save(goal)).thenReturn(goal);

        goalService.delete(goal);

        verify(goalValidator).validToDelete(goal);
        assertThat(goal.getDeletion().getDeletedAt()).isNotNull();
        verify(goalRepository).save(goal);
    }
}
