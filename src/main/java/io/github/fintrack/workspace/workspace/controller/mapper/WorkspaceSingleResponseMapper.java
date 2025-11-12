package io.github.fintrack.workspace.workspace.controller.mapper;

import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkspaceSingleResponseMapper {
    @Mapping(target = "created", expression = "java( entity.getCreation().getCreatedAt() )")
    WorkspaceSingleResponse toDto(Workspace entity);
}
