package io.github.fintrack._common.config.jpa;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
@RequiredArgsConstructor
public class CustomAuditorAware implements AuditorAware<User> {

    private final AuthService authService;

    @Override
    @NonNull
    public Optional<User> getCurrentAuditor() {
        User user = authService.getUserLoggedIn();
        return Optional.ofNullable(user);
    }
}