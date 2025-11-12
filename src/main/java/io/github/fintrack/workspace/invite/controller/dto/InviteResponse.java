package io.github.fintrack.workspace.invite.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.fintrack.auth.controller.dto.UserResponse;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record InviteResponse (
        UUID id,
        Status status,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime created,
        WorkspaceSingleResponse workspace,
        UserResponse from,
        UserResponse to
) { }
