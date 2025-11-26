package io.github.fintrack.workspace.workspace.controller.mapper;

import io.github.fintrack.workspace.member.controller.mapper.MemberResponseMapper;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = {MemberResponseMapper.class}
)
public abstract class WorkspaceMapper {

    @Autowired
    protected MemberResponseMapper memberResponseMapper;

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public abstract Workspace toEntity(WorkspaceRequest request);

    @Mapping(target = "created", expression = "java( entity.getCreation().getCreatedAt() )")
    @Mapping(target = "members", source = "members")
    @Mapping(target = "owner", expression = "java( memberResponseMapper.toDto(entity.getOwner()) )")
    public abstract WorkspaceDetailsResponse toDetailsResponse(Workspace entity);

    @Mapping(target = "created", expression = "java( entity.getCreation().getCreatedAt() )")
    public abstract WorkspaceSingleResponse toSingleResponse(Workspace entity);
}
