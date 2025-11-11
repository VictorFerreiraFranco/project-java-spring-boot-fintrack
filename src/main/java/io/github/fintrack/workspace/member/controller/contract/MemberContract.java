package io.github.fintrack.workspace.member.controller.contract;

import io.github.fintrack.workspace.member.controller.dto.MemberResponse;
import io.github.fintrack.workspace.member.controller.mapper.MemberResponseMapper;
import io.github.fintrack.workspace.member.exception.MemberNotFoundException;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.service.MemberService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberContract {

    private final MemberService memberService;
    private final WorkspaceService workspaceService;
    private MemberResponseMapper memberResponseMapper;

    public MemberResponse findById(String id) {
        return memberResponseMapper.toDto(
                memberService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                        .orElseThrow(MemberNotFoundException::new)
        );
    }

    public List<MemberResponse> findByWorkspaceId(String workspaceId) {
        return workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .map(workspace -> {
                    return memberService.findAllByWorkspaceAndDeletedAtIsNull(workspace)
                          .stream()
                          .map(memberResponseMapper::toDto)
                          .toList();

                })
                .orElseThrow(WorkspaceNotFoundException::new);
    }

    public void delete(String id) {
        memberService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .map(member -> {
                    memberService.delete(member);
                    return member;
                })
                .orElseThrow(MemberNotFoundException::new);
    }
}
