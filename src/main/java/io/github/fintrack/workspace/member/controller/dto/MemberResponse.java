package io.github.fintrack.workspace.member.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.fintrack.workspace.member.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MemberResponse {
    private UUID id;
    private Role role;

    @JsonProperty("user_id")
    private UUID userId;

    private String name;
    private String email;
}
