package io.github.fintrack.workspace.financial.category.validator;

import io.github.fintrack.common.exception.DuplicateRecordException;
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
            throw new DuplicateRecordException("Category already exists");

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(category.getWorkspace());
    }

    public void validToDelete(Category category) {
        workspaceValidator.validUserLoggedInIsMemberByWorkspace(category.getWorkspace());
    }

    public boolean categoryExists(Category category) {
        Optional<Category> categoryFund = categoryRepository
                .findByWorkspaceAndDescriptionIgnoreCaseAndTypeAndDeletion_DeletedAtIsNull(
                        category.getWorkspace(),
                        category.getDescription(),
                        category.getType()
                );

        if (category.getId() == null)
            return categoryFund.isPresent();

        return categoryFund.isPresent() && !category.getId().equals(categoryFund.get().getId());
    }
}
