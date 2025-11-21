package io.github.fintrack.workspace.member.controller.contract;

import io.github.fintrack.workspace.member.controller.dto.MemberResponse;
import io.github.fintrack.workspace.member.controller.mapper.MemberResponseMapper;
import io.github.fintrack.workspace.member.exception.MemberNotFoundException;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.service.MemberService;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberContract {

    private final MemberService memberService;
    private final WorkspaceService workspaceService;
    private final MemberResponseMapper memberResponseMapper;

    @Transactional(readOnly = true)
    public MemberResponse getById(String id) {
        return memberResponseMapper.toDto(
                memberService.findByIdAndValidateExistence(UUID.fromString(id))
        );
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getByWorkspaceId(String workspaceId) {
        return memberService.findAllByWorkspaceAndDeletedAtIsNull(
                        workspaceService.findByIdAndValidateExistenceAndMembership(UUID.fromString(workspaceId))
                )
                .stream()
                .map(memberResponseMapper::toDto)
                .toList();
    }

    @Transactional
    public void delete(String id) {
        memberService.delete(
                memberService.findByIdAndValidateExistence(UUID.fromString(id))
        );
    }
}
