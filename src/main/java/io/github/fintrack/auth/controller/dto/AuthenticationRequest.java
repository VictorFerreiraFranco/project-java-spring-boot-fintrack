package io.github.fintrack.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
    @NotBlank(message = "field required")
    @Email
    String email,

    @NotBlank(message = "field required")
    @Size(min = 5, message = "the field must contain min {min} characters")
    String password
) {}
