package io.github.fintrack.workspace.financial.category.service;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryFilter;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.financial.category.repository.specification.CategorySpecification;
import io.github.fintrack.workspace.financial.category.validator.CategoryValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    private final AuthService authService;

    public Optional<Category> findByIdAndDeletedAtIsNull(UUID id) {
        return categoryRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public List<Category> searchAllByWorkspace(
            Workspace workspace, CategoryFilter filter
    ) {
        return categoryRepository.findAll(
                CategorySpecification.deletedAtIsNull()
                        .and(CategorySpecification.workspaceEqual(workspace))
                        .and(CategorySpecification.applyFilter(filter))
        );
    }

    public Category save(Category category) {
        categoryValidator.validToSave(category);
        return categoryRepository.save(category);
    }

    public void delete(Category category) {
        categoryValidator.validToDelete(category);
        category.getDeletion().markAsDeleted(authService.getUserLoggedIn());
        categoryRepository.save(category);
    }
}
