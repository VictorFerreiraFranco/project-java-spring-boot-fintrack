package io.github.fintrack.workspace.workspace.validator;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.workspace.exception.CannotDeleteMainWorkspaceException;
import io.github.fintrack.workspace.workspace.exception.UserIsNotMemberWorkspaceException;
import io.github.fintrack.workspace.workspace.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WorkspaceValidator {

    private final AuthService authService;

    public void validToDelete(Workspace workspace) {
        if (isMain(workspace))
            throw new CannotDeleteMainWorkspaceException();
    }

    public void validUserLoggedInIsMemberByWorkspace(Workspace workspace) {
        if (userLoggedInIsNotMemberByWorkspace(workspace))
            throw new UserIsNotMemberWorkspaceException();
    }

    public boolean isMain(Workspace workspace) {
        return Type.MAIN.equals(workspace.getType());
    }

    public boolean userIsMember(Workspace workspace, User user) {
        return workspace.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(user.getId()));
    }

    public boolean userLoggedInIsNotMemberByWorkspace(Workspace workspace) {
        return !this.userIsMember(workspace, authService.getUserLoggedIn());
    }
}
