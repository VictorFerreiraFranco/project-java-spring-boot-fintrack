package io.github.fintrack.workspace.member.controller.mapper;

import io.github.fintrack.workspace.member.controller.dto.MemberResponse;
import io.github.fintrack.workspace.member.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberResponseMapper {

    @Mapping(target = "userId", expression = "java( member.getUser().getId() )")
    @Mapping(target = "name", expression = "java( member.getUser().getName() )")
    @Mapping(target = "email", expression = "java( member.getUser().getEmail() )")
    MemberResponse toDto(Member member);
}
