package io.github.fintrack.workspace.controller.mapper;

import io.github.fintrack.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.model.Workspace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceRequestMapper {
    Workspace toEntity(WorkspaceRequest request);
}
