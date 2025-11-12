package io.github.fintrack.workspace.financial.category.validator;

import io.github.fintrack.workspace.financial.category.exception.CategoryNotFoundException;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.financial.category.repository.CategoryRepository;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryValidator {

    private final CategoryRepository categoryRepository;
    private final WorkspaceValidator workspaceValidator;

    public void validToSave(Category category) {
        if (categoryExists(category))
            throw new CategoryNotFoundException();

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(category.getWorkspace());
    }

    public void validToDelete(Category category) {
        workspaceValidator.validUserLoggedInIsMemberByWorkspace(category.getWorkspace());
    }

    public boolean categoryExists(Category category) {
        Optional<Category> optionalCategory = categoryRepository
                .findByWorkspaceAndDescriptionIgnoreCaseAndDeletion_DeletedAtIsNull(
                        category.getWorkspace(),
                        category.getDescription()
                );

        if (category.getId() == null)
            return optionalCategory.isPresent();

        return optionalCategory.isPresent() && !category.getId().equals(optionalCategory.get().getId());
    }
}
