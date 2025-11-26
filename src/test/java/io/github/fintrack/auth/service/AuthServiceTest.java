package io.github.fintrack.auth.service;

import io.github.fintrack.auth.model.Role;
import io.github.fintrack.auth.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should return logged user when principal is User")
    void shouldReturnLoggedUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@test.com")
                .password("123")
                .role(Role.USER)
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        User result = authService.getUserLoggedIn();

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("Should return null when principal is not User")
    void shouldReturnNullWhenPrincipalIsNotUser() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("not-a-user", null, null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        User result = authService.getUserLoggedIn();

        assertThat(result).isNull();
    }
}
