package io.github.fintrack.workspace.member.controller.dto;

import io.github.fintrack.auth.controller.dto.UserResponse;
import io.github.fintrack.workspace.member.model.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberResponse (
        UUID id,
        Role role,
        LocalDateTime created,
        UserResponse user
) {}
