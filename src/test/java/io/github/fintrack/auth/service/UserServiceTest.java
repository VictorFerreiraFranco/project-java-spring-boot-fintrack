package io.github.fintrack.auth.service;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.auth.service.validator.UserValidator;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserValidator userValidator = mock(UserValidator.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final WorkspaceService workspaceService = mock(WorkspaceService.class);

    private final UserService userService = new UserService(
            userRepository, userValidator, passwordEncoder, workspaceService
    );

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        String email = "test@email.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should validate and save user")
    void shouldValidateAndSaveUser() {
        User user = new User();

        userService.save(user);

        verify(userValidator).validToSave(user);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should register user with encoded password and create default workspace")
    void shouldRegisterUser() {
        User user = new User();
        user.setPassword("123");

        when(passwordEncoder.encode("123"))
                .thenReturn("encoded-pass");

        userService.registerUser(user);

        assertThat(user.getPassword()).isEqualTo("encoded-pass");

        assertThat(user.getRole()).isEqualTo(Role.USER);

        verify(userValidator).validToSave(user);
        verify(userRepository).save(user);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(workspaceService).createMain(userCaptor.capture());

        assertThat(userCaptor.getValue()).isEqualTo(user);
    }
}
