package io.github.fintrack.workspace.workspace.controller.mapper;

import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceRequestMapper {
    Workspace toEntity(WorkspaceRequest request);
}
