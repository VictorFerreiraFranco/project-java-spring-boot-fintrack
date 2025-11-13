package io.github.fintrack.workspace.financial.goal.exception;

import io.github.fintrack.common.exception.FinTrackMappedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GoalException extends FinTrackMappedException {
    public GoalException(String message) {
        super(message);
    }
}
