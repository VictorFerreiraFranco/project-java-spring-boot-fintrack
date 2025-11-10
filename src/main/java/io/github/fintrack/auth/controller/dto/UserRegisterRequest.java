package io.github.fintrack.auth.controller.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    @NotBlank(message = "field required")
    @Size(min = 3, max = 100, message = "the field must contain between {min} and {max} characters")
    private String name;

    @NotBlank(message = "field required")
    @Email
    private String email;

    @NotBlank(message = "field required")
    @Size(min = 5, message = "the field must contain min {min} characters")
    private String password;
}
