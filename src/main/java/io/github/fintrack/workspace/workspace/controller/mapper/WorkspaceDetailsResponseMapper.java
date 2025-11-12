package io.github.fintrack.workspace.workspace.controller.mapper;

import io.github.fintrack.workspace.member.controller.dto.MemberResponse;
import io.github.fintrack.workspace.member.controller.mapper.MemberResponseMapper;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {MemberResponseMapper.class}
)
public abstract class WorkspaceDetailsResponseMapper {

    @Autowired
    protected MemberResponseMapper memberResponseMapper;

    @Mapping(target = "created", expression = "java( entity.getCreation().getCreatedAt() )")
    @Mapping(target = "members", source = "members")
    @Mapping(target = "owner", expression = "java(findOwner(entity.getMembers()))")
    public abstract WorkspaceDetailsResponse toDto(Workspace entity);

    protected MemberResponse findOwner(List<Member> members) {
        if (members == null) return null;
        return members.stream()
                .filter(Member::isOwner)
                .findFirst()
                .map(memberResponseMapper::toDto)
                .orElse(null);
    }
}
