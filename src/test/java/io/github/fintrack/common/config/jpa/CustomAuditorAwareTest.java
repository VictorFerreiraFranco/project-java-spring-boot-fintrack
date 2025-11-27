package io.github.fintrack.common.config.jpa;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomAuditorAwareTest {

    private CustomAuditorAware auditorAware;
    private AuthService authService;

    @BeforeEach
    void setup() {
        authService = mock(AuthService.class);
        auditorAware = new CustomAuditorAware(
                authService
        );
    }

    @Test
    @DisplayName("Should return current auditor")
    void shouldReturnCurrentAuditor() {
        User user = new User();

        when(authService.getUserLoggedIn())
                .thenReturn(user);

        Optional<User> currentAuditor = auditorAware.getCurrentAuditor();

        assertThat(currentAuditor).isPresent();
        assertThat(currentAuditor.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should return null if no current auditor")
    void shouldReturnNullIfNoCurrentAuditor() {
        when(authService.getUserLoggedIn())
                .thenReturn(null);

        Optional<User> currentAuditor = auditorAware.getCurrentAuditor();

        assertThat(currentAuditor).isNotPresent();
    }
}
