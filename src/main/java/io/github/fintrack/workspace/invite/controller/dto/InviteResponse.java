package io.github.fintrack.workspace.invite.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.fintrack.workspace.invite.model.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InviteResponse {
    private UUID id;
    private Status status;
    private UUID workspace;

    @JsonProperty("workspace_name")
    private String workspaceName;

    private UUID from;
    @JsonProperty("from_email")
    private String fromEmail;
    @JsonProperty("from_name")
    private String fromName;

    private UUID to;
    @JsonProperty("to_email")
    private String toEmail;
    @JsonProperty("to_name")
    private String toName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
