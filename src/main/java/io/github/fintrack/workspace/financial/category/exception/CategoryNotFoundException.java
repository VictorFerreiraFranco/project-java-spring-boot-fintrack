package io.github.fintrack.workspace.financial.category.exception;

import io.github.fintrack.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException() {
        super("Category already exists");
    }
}
