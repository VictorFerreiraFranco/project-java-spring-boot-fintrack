package io.github.fintrack.workspace.member.controller.contract;

import io.github.fintrack.workspace.member.controller.dto.MemberResponse;
import io.github.fintrack.workspace.member.controller.mapper.MemberResponseMapper;
import io.github.fintrack.workspace.member.exception.MemberNotFoundException;
import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.service.MemberService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
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
    private final WorkspaceValidator workspaceValidator;

    @Transactional(readOnly = true)
    public MemberResponse getById(String id) {
        return memberResponseMapper.toDto(
                this.findById(id)
        );
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getByWorkspaceId(String workspaceId) {
        return memberService.findAllByWorkspaceAndDeletedAtIsNull(this.findWorkspaceById(workspaceId))
                .stream()
                .map(memberResponseMapper::toDto)
                .toList();
    }

    @Transactional
    public void delete(String id) {
        memberService.delete(
                this.findById(id)
        );
    }

    private Member findById(String id) {
        return memberService.findByIdAndDeletedAtIsNull(UUID.fromString(id))
                .orElseThrow(MemberNotFoundException::new);
    }

    private Workspace findWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(workspace);

        return workspace;
    }
}
