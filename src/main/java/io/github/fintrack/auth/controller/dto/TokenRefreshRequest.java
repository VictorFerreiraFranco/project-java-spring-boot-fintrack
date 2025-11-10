package io.github.fintrack.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshRequest {
    @NotBlank(message = "field required")
    @JsonProperty("refresh_token")
    private String refreshToken;
}
