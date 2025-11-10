package io.github.fintrack.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest (
    @NotBlank(message = "field required")
    @JsonProperty("refresh_token")
    String refreshToken
) {}
