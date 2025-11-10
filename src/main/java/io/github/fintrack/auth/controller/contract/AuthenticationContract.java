package io.github.fintrack.auth.controller.contract;

import io.github.fintrack._common.config.auth.jwt.JwtService;
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
import io.github.fintrack.workspace.model.Type;
import io.github.fintrack.workspace.model.Workspace;
import io.github.fintrack.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthenticationContract {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final WorkspaceService workspaceService;
    private final RegisterRequestMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);

        Workspace workspace = Workspace.builder()
                .name("Principal")
                .type(Type.MAIN)
                .createdBy(user)
                .build();
        workspaceService.save(workspace);

        return buildAuthenticationResponseWithNewToken(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
            )
        );

        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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
