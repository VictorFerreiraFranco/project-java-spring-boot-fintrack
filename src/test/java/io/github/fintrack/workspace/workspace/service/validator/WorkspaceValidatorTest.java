package io.github.fintrack.workspace.workspace.service.validator;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.workspace.exception.CannotDeleteMainWorkspaceException;
import io.github.fintrack.workspace.workspace.exception.UserIsNotMemberWorkspaceException;
import io.github.fintrack.workspace.workspace.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkspaceValidatorTest {

    private final AuthService authService = mock(AuthService.class);

    private final WorkspaceValidator validator = new WorkspaceValidator(authService);

    @Test
    @DisplayName("Should throw exception when deleting main workspace")
    void shouldThrowExceptionWhenDeletingMainWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setType(Type.MAIN);

        assertThatThrownBy(() -> validator.validToDelete(workspace))
                .isInstanceOf(CannotDeleteMainWorkspaceException.class);
    }

    @Test
    @DisplayName("Should allow deleting other workspace")
    void shouldAllowDeletingOtherWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setType(Type.OTHER);

        assertThatCode(() -> validator.validToDelete(workspace))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should allow creating main workspace")
    void shouldReturnTrueWhenWorkspaceIsMain() {
        Workspace workspace = new Workspace();
        workspace.setType(Type.MAIN);

        assertThat(validator.isMain(workspace)).isTrue();
    }

    @Test
    @DisplayName("Should return false when workspace is not main")
    void shouldReturnFalseWhenWorkspaceIsNotMain() {
        Workspace workspace = new Workspace();
        workspace.setType(Type.OTHER);

        assertThat(validator.isMain(workspace)).isFalse();
    }

    @Test
    @DisplayName("Should return true when user is member")
    void shouldReturnTrueWhenUserIsMember() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Member member = new Member();
        member.setUser(user);

        Workspace workspace = new Workspace();
        workspace.setMembers(List.of(member));

        assertThat(validator.userIsMember(workspace, user)).isTrue();
    }

    @Test
    @DisplayName("Should return false when user is not member")
    void shouldReturnFalseWhenUserIsNotMember() {
        User user = new User();
        user.setId(UUID.randomUUID());

        User other = new User();
        other.setId(UUID.randomUUID());

        Member member = new Member();
        member.setUser(other);

        Workspace workspace = new Workspace();
        workspace.setMembers(List.of(member));

        assertThat(validator.userIsMember(workspace, user)).isFalse();
    }

    @Test
    @DisplayName("Should return false when logged user is member")
    void shouldReturnFalseWhenLoggedUserIsMember() {
        User logged = new User();
        logged.setId(UUID.randomUUID());

        Member member = new Member();
        member.setUser(logged);

        Workspace workspace = new Workspace();
        workspace.setMembers(List.of(member));

        when(authService.getUserLoggedIn()).thenReturn(logged);

        assertThat(validator.userLoggedInIsNotMemberByWorkspace(workspace)).isFalse();
    }

    @Test
    @DisplayName("Should return true when logged user is not member")
    void shouldReturnTrueWhenLoggedUserIsNotMember() {
        User logged = new User();
        logged.setId(UUID.randomUUID());

        User other = new User();
        other.setId(UUID.randomUUID());

        Member member = new Member();
        member.setUser(other);

        Workspace workspace = new Workspace();
        workspace.setMembers(List.of(member));

        when(authService.getUserLoggedIn()).thenReturn(logged);

        assertThat(validator.userLoggedInIsNotMemberByWorkspace(workspace)).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when logged user is not member")
    void shouldThrowExceptionWhenLoggedUserIsNotMember() {
        User logged = new User();
        logged.setId(UUID.randomUUID());

        User other = new User();
        other.setId(UUID.randomUUID());

        Member member = new Member();
        member.setUser(other);

        Workspace workspace = new Workspace();
        workspace.setMembers(List.of(member));

        when(authService.getUserLoggedIn()).thenReturn(logged);

        assertThatThrownBy(() -> validator.validUserLoggedInIsMemberByWorkspace(workspace))
                .isInstanceOf(UserIsNotMemberWorkspaceException.class);
    }

    @Test
    @DisplayName("Should not throw exception when logged user is member")
    void shouldNotThrowExceptionWhenLoggedUserIsMember() {
        User logged = new User();
        logged.setId(UUID.randomUUID());

        Member member = new Member();
        member.setUser(logged);

        Workspace workspace = new Workspace();
        workspace.setMembers(List.of(member));

        when(authService.getUserLoggedIn()).thenReturn(logged);

        assertThatCode(() -> validator.validUserLoggedInIsMemberByWorkspace(workspace))
                .doesNotThrowAnyException();
    }
}
