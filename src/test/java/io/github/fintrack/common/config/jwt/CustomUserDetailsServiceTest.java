package io.github.fintrack.common.config.jwt;

import io.github.fintrack.auth.exception.UserNotFoundException;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.UserService;
import io.github.fintrack.common.config.CustomUserDetailsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;


    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    @DisplayName("Should load user by username")
    void shouldLoadUserByUsername() {
        User user = User.builder()
                .email("teste@gmail.com")
                .build();

        when(userService.findByEmail(any()))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userService.findByEmail(any()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("notfound@email.com"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
