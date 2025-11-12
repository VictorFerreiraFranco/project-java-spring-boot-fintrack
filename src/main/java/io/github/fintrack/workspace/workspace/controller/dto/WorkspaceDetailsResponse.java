package io.github.fintrack.workspace.workspace.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.fintrack.workspace.member.controller.dto.MemberResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WorkspaceDetailsResponse(
        UUID id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime created,
        MemberResponse owner,
        List<MemberResponse> members
) {}