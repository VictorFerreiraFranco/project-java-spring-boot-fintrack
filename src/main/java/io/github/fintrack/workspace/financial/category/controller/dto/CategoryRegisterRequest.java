package io.github.fintrack.workspace.financial.category.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import jakarta.validation.constraints.NotBlank;

public class CategoryRegisterRequest extends CategoryUpdateRequest {
    @ValidUUID
    @NotBlank
    @JsonProperty("workspace_id")
    public String workspaceId;
}
