package io.github.fintrack.workspace.invite.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteRequest (
        @NotNull
        @JsonProperty("workspace_id")
        @ValidUUID
        String workspaceId,

        @NotBlank
        @Email
        String email
) {}
