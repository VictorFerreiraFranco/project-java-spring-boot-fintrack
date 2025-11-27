package io.github.fintrack.auth.controller.contract;

import io.github.fintrack.auth.controller.dto.*;
import io.github.fintrack.auth.controller.mapper.RegisterRequestMapper;
import io.github.fintrack.auth.controller.mapper.UserResponseMapper;
import io.github.fintrack.auth.exception.TokenRefreshException;
import io.github.fintrack.auth.exception.UserNotFoundException;
import io.github.fintrack.auth.model.RefreshToken;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.AuthService;
import io.github.fintrack.auth.service.RefreshTokenService;
import io.github.fintrack.auth.service.UserService;
import io.github.fintrack.common.config.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationContractTest {

    private UserService userService;
    private RefreshTokenService refreshTokenService;
    private JwtService jwtService;
    private AuthService authService;
    private RegisterRequestMapper registerRequestMapper;
    private UserResponseMapper userResponseMapper;

    private AuthenticationContract contract;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        jwtService = mock(JwtService.class);
        authService = mock(AuthService.class);
        registerRequestMapper = mock(RegisterRequestMapper.class);
        userResponseMapper = mock(UserResponseMapper.class);

        contract = new AuthenticationContract(
                userService,
                refreshTokenService,
                jwtService,
                authService,
                mock(AuthenticationManager.class),
                registerRequestMapper,
                userResponseMapper
        );
    }

    @Test
    @DisplayName("Should register user and return authentication response")
    void shouldRegisterUserAndReturnAuthenticationResponse() {
        String accessToken = "access123";
        String refreshToken = "refresh123";

        RegisterRequest request = new RegisterRequest("John", "john@email.com", "123");

        User user = new User();

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .build();

        when(registerRequestMapper.toEntity(request))
                .thenReturn(user);

        when(userService.registerUser(user))
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenReturn(accessToken);

        when(refreshTokenService.createByUser(user))
                .thenReturn(token);

        AuthenticationResponse response = contract.register(request);

        assertThat(response.accessToken()).isEqualTo(accessToken);
        assertThat(response.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("Should authenticate user and return authentication response")
    void shouldAuthenticateUser() {
        String accessToken = "access123";
        String refreshToken = "refresh123";

        AuthenticationRequest request = new AuthenticationRequest("john@email.com", "123");

        User user = new User();

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .build();

        when(userService.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(user))
                .thenReturn(accessToken);

        when(refreshTokenService.createByUser(user))
                .thenReturn(token);

        AuthenticationResponse response = contract.authenticate(request);

        assertThat(response.accessToken()).isEqualTo(accessToken);
        assertThat(response.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException if user not exists")
    void shouldThrowUserNotFoundExceptionIfUserNotExists() {
        AuthenticationRequest request = new AuthenticationRequest("notfound@email.com", "123");

        when(userService.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> contract.authenticate(request));
    }

    @Test
    @DisplayName("Should refresh token")
    void shouldRefreshToken() {
        String oldToken = "old-refresh";
        String newToken = "new-refresh";

        User user = new User();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(oldToken)
                .user(user)
                .build();

        when(refreshTokenService.findByToken(oldToken))
                .thenReturn(Optional.of(refreshToken));

        when(refreshTokenService.verifyExpiration(refreshToken))
                .thenReturn(refreshToken);

        when(jwtService.generateToken(user))
                .thenReturn(newToken);

        AuthenticationResponse response = contract.refreshToken(new TokenRefreshRequest(oldToken));

        assertThat(response.accessToken()).isEqualTo(newToken);
        assertThat(response.refreshToken()).isEqualTo(oldToken);
    }

    @Test
    @DisplayName("Should throw TokenRefreshException if token not found")
    void shouldThrowTokenRefreshExceptionIfTokenNotFound() {
        String token = "notfound";

        when(refreshTokenService.findByToken(token))
                .thenReturn(Optional.empty());

        assertThrows(TokenRefreshException.class, () -> contract.refreshToken(new TokenRefreshRequest(token)));
    }

    @Test
    @DisplayName("Should return current user")
    void shouldGetCurrentUser() {
        User user = new User();

        var dto = mock(UserResponse.class);

        when(authService.getUserLoggedIn())
                .thenReturn(user);

        when(userResponseMapper.toDto(user))
                .thenReturn(dto);

        assertThat(contract.getCurrentUser()).isEqualTo(dto);
    }
}
