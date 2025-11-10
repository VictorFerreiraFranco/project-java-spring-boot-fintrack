package io.github.fintrack.workspace.controller.mapper;

import io.github.fintrack.workspace.controller.dto.WorkspaceResponse;
import io.github.fintrack.workspace.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceResponseMapper {

    @Mapping(target = "created", expression = "java( entity.getCreation().getCreatedAt() )")
    WorkspaceResponse toDto(Workspace entity);
}
