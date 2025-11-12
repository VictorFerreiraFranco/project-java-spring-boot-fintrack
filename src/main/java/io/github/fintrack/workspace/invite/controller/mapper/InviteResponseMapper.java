package io.github.fintrack.workspace.invite.controller.mapper;

import io.github.fintrack.auth.controller.mapper.UserResponseMapper;
import io.github.fintrack.workspace.invite.controller.dto.InviteResponse;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {WorkspaceMapper.class, UserResponseMapper.class}
)
public interface InviteResponseMapper {

    @Mapping(target = "created", expression = "java( invite.getCreation().getCreatedAt() )")
    InviteResponse toDto(Invite invite);
}
