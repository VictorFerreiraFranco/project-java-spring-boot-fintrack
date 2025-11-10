package io.github.fintrack.auth.controller;

import io.github.fintrack.auth.controller.dto.AuthenticationRequest;
import io.github.fintrack.auth.controller.dto.AuthenticationResponse;
import io.github.fintrack.auth.controller.dto.UserRegisterRequest;
import io.github.fintrack.auth.controller.dto.TokenRefreshRequest;
import io.github.fintrack.auth.controller.contract.AuthenticationContract;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationContract contract;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody UserRegisterRequest request
    ) {
        return ResponseEntity.ok(contract.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(contract.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        return ResponseEntity.ok(contract.refreshToken(request));
    }
}
