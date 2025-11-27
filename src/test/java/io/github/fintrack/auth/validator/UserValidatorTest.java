package io.github.fintrack.auth.validator;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.repository.UserRepository;
import io.github.fintrack.common.exception.DuplicateRecordException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class UserValidatorTest {

    private final UserRepository userRepository = mock(UserRepository.class);

    private final UserValidator userValidator = new UserValidator(userRepository);

    @Test
    @DisplayName("Should throw exception if user exists")
    void shouldThrowIfUserExists() {
        String email = "test@example.com";

        User existingUser = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .name("Test")
                .password("123")
                .role(Role.USER)
                .build();

        User newUser = User.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(existingUser));

        Assertions.assertThatThrownBy(() -> userValidator.validToSave(newUser))
                .isInstanceOf(DuplicateRecordException.class);
    }

    @Test
    @DisplayName("Should not throw exception if user does not exist")
    void shouldNotThrowIfUserDoesNotExist() {
        String email = "test@example.com";

        User newUser = User.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        userValidator.validToSave(newUser);

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should not consider the same user as duplicate when updating")
    void shouldAllowSameUserUpdate() {
        UUID id = UUID.randomUUID();
        String email = "test@example.com";

        User existingUser = User.builder()
                .id(id)
                .email(email)
                .build();

        User updatingUser = User.builder()
                .id(id)
                .email(email)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(existingUser));

        userValidator.validToSave(updatingUser);

        verify(userRepository).findByEmail(email);
    }
}
