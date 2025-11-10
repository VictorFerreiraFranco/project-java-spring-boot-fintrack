package io.github.fintrack.auth.service;

import io.github.fintrack.auth.exception.UnauthorizedException;
import io.github.fintrack.auth.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthService {
    public User getUserLoggedIn() throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        throw new UnauthorizedException("Unauthorized");
    }
}
