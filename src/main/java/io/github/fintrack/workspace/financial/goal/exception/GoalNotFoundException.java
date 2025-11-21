package io.github.fintrack.workspace.financial.goal.exception;

import io.github.fintrack.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GoalNotFoundException extends ResourceNotFoundException {
    public GoalNotFoundException() {
        super("Goal not found");
    }
}
