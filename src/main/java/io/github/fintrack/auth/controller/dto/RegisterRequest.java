package io.github.fintrack.auth.controller.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank(message = "field required")
    @Size(min = 3, max = 100, message = "the field must contain between {min} and {max} characters")
    String name,

    @NotBlank(message = "field required")
    @Email
    String email,

    @NotBlank(message = "field required")
    @Size(min = 5, message = "the field must contain min {min} characters")
    String password
) {}
