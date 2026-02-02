package io.github.fintrack.workspace.financial.category.service;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryFilter;
import io.github.fintrack.workspace.financial.category.exception.CategoryNotFoundException;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.financial.category.service.validator.CategoryValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryValidator categoryValidator = mock(CategoryValidator.class);
    private final AuthService authService = mock(AuthService.class);
    private final WorkspaceValidator workspaceValidator = mock(WorkspaceValidator.class);

    private final CategoryService categoryService = new CategoryService(
            categoryRepository, categoryValidator, authService, workspaceValidator
    );

    @Test
    @DisplayName("Should find by id and deletedAt is null")
    void shouldFindByIdAndDeletedAtIsNull() {
        Category category = new Category();
        category.setId(UUID.randomUUID());

        when(categoryRepository.findByIdAndDeletionDeletedAtIsNull(category.getId()))
                .thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.findByIdAndDeletedAtIsNull(category.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(category.getId());

        verify(categoryRepository).findByIdAndDeletionDeletedAtIsNull(category.getId());
    }

    @Test
    @DisplayName("Should search all categories by workspace with filter")
    void shouldSearchAllCategoriesByWorkspaceWithFilter() {
        Workspace workspace = new Workspace();
        CategoryFilter filter = new CategoryFilter("Test", "White", Type.INCOME.toString());

        List<Category> expected = List.of(new Category(), new Category());

        when(categoryRepository.findAll(any(Specification.class)))
                .thenReturn(expected);

        List<Category> result = categoryService.searchAllByWorkspace(workspace, filter);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should return category when exists")
    void shouldReturnCategoryWhenExists() {
        UUID id = UUID.randomUUID();
        Category category = new Category();

        when(categoryRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(category));

        Category result = categoryService.findByIdAndValidateExistence(id);

        assertThat(result).isEqualTo(category);
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void shouldThrowExceptionWhenCategoryNotFound() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                categoryService.findByIdAndValidateExistence(id)
        ).isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Should return category when exists and user is member")
    void shouldReturnCategoryWhenExistsAndUserIsMember() {
        UUID id = UUID.randomUUID();

        Workspace workspace = new Workspace();
        Category category = new Category();
        category.setWorkspace(workspace);

        when(categoryRepository.findByIdAndDeletionDeletedAtIsNull(id))
                .thenReturn(Optional.of(category));

        Category result = categoryService.findByIdAndValidateExistenceAndMembership(id);

        assertThat(result).isEqualTo(category);
        verify(workspaceValidator).userLoggedInIsNotMemberByWorkspace(workspace);
    }

    @Test
    @DisplayName("Should save category")
    void shouldSaveCategory() {
        Category category = new Category();
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.save(category);

        assertThat(result).isEqualTo(category);
        verify(categoryValidator).validToSave(category);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Should delete category")
    void shouldDeleteCategory() {
        User loggedUser = new User();
        when(authService.getUserLoggedIn()).thenReturn(loggedUser);

        Category category = new Category();

        when(categoryRepository.save(category)).thenReturn(category);

        categoryService.delete(category);

        verify(categoryValidator).validToDelete(category);
        assertThat(category.getDeletion().getDeletedAt()).isNotNull();
        verify(categoryRepository).save(category);
    }
}
