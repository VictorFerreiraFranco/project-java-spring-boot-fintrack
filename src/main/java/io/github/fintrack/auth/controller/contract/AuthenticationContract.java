package io.github.fintrack.auth.controller.contract;

import io.github.fintrack.auth.exception.UserNotFoundException;
import io.github.fintrack.common.config.jwt.JwtService;
import io.github.fintrack.auth.controller.dto.AuthenticationRequest;
import io.github.fintrack.auth.controller.dto.AuthenticationResponse;
import io.github.fintrack.auth.controller.dto.RegisterRequest;
import io.github.fintrack.auth.controller.dto.TokenRefreshRequest;
import io.github.fintrack.auth.controller.mapper.RegisterRequestMapper;
import io.github.fintrack.auth.exception.TokenRefreshException;
import io.github.fintrack.auth.model.RefreshToken;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.auth.service.RefreshTokenService;
import io.github.fintrack.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationContract {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RegisterRequestMapper registerRequestMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse register(RegisterRequest request) {
        return buildAuthenticationResponseWithNewToken(
            userService.registerUser(
                registerRequestMapper.toEntity(request)
            )
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
            )
        );

        User user = userService.findByEmail(request.email())
                .orElseThrow(UserNotFoundException::new);

        return buildAuthenticationResponseWithNewToken(user);
    }

    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(requestRefreshToken)
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    private AuthenticationResponse buildAuthenticationResponseWithNewToken(User user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createByUser(user).getToken();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
