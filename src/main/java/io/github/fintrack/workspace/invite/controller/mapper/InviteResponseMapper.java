package io.github.fintrack.workspace.invite.controller.mapper;

import io.github.fintrack.workspace.invite.controller.dto.InviteResponse;
import io.github.fintrack.workspace.invite.model.Invite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InviteResponseMapper {

    @Mapping(target = "workspace", expression = "java( invite.getWorkspace().getId() )")
    @Mapping(target = "workspaceName", expression = "java( invite.getWorkspace().getName() )")
    @Mapping(target = "from", expression = "java( invite.getFrom().getId() )")
    @Mapping(target = "fromEmail", expression = "java( invite.getFrom().getEmail() )")
    @Mapping(target = "fromName", expression = "java( invite.getFrom().getName() )")
    @Mapping(target = "to", expression = "java( invite.getTo().getId() )")
    @Mapping(target = "toEmail", expression = "java( invite.getTo().getEmail() )")
    @Mapping(target = "toName", expression = "java( invite.getTo().getName() )")
    @Mapping(target = "created", expression = "java( invite.getCreation().getCreatedAt() )")
    InviteResponse toDto(Invite invite);
}
