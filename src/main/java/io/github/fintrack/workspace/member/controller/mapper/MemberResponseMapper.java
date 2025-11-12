package io.github.fintrack.workspace.member.controller.mapper;

import io.github.fintrack.auth.controller.mapper.UserResponseMapper;
import io.github.fintrack.workspace.member.controller.dto.MemberResponse;
import io.github.fintrack.workspace.member.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {UserResponseMapper.class}
)
public interface MemberResponseMapper {

    @Mapping(target = "created", expression = "java( member.getCreation().getCreatedAt() )")
    MemberResponse toDto(Member member);
}
