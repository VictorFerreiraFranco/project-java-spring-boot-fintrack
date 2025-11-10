package io.github.fintrack.workspace.workspace.controller.mapper;

import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceResponse;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceResponseMapper {

    @Mapping(target = "created", expression = "java( entity.getCreation().getCreatedAt() )")
    WorkspaceResponse toDto(Workspace entity);
}
