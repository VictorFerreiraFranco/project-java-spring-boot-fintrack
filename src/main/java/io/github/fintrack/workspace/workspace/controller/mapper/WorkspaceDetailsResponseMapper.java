package io.github.fintrack.workspace.workspace.controller.mapper;

import io.github.fintrack.workspace.member.controller.mapper.MemberResponseMapper;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(
        componentModel = "spring",
        uses = {MemberResponseMapper.class}
)
public abstract class WorkspaceDetailsResponseMapper {

    @Autowired
    protected MemberResponseMapper memberResponseMapper;

    @Mapping(target = "created", expression = "java( entity.getCreation().getCreatedAt() )")
    @Mapping(target = "members", source = "members")
    @Mapping(target = "owner", expression = "java( entity.getOwner() )")
    public abstract WorkspaceDetailsResponse toDto(Workspace entity);
}
