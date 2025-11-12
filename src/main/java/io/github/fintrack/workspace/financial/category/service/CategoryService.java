package io.github.fintrack.workspace.financial.category.service;

import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.financial.category.validator.CategoryValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
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
    private final WorkspaceValidator workspaceValidator;
    private final AuthService authService;

    public Optional<Category> findByIdAndDeletedAtIsNull(UUID id) {
        return categoryRepository.findByIdAndDeletion_DeletedAtIsNull(id);
    }

    public List<Category> findAllByWorkspaceAndDeletedAtIsNull(Workspace workspace) {
        return categoryRepository.findAllByWorkspaceAndDeletion_DeletedAtIsNull(workspace);
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
