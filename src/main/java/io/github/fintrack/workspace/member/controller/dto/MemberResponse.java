package io.github.fintrack.workspace.member.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.fintrack.auth.controller.dto.UserResponse;
import io.github.fintrack.workspace.member.model.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record MemberResponse (
        UUID id,
        Role role,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime created,
        UserResponse user
) {}
