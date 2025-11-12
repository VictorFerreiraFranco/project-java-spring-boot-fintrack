package io.github.fintrack.auth.controller.dto;

import io.github.fintrack.auth.model.Role;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    Role role
) {}
