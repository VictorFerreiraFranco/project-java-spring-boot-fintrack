package io.github.fintrack.workspace.validator;

import io.github.fintrack.workspace.exception.CannotDeleteMainWorkspaceException;
import io.github.fintrack.workspace.model.Type;
import io.github.fintrack.workspace.model.Workspace;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkspaceValidator {
    public void validateDelete(Workspace workspace) {
        if (isMain(workspace))
            throw new CannotDeleteMainWorkspaceException();
    }

    public boolean isMain(Workspace workspace) {
        return Type.MAIN.equals(workspace.getType());
    }
}
