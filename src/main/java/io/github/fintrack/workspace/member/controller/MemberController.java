package io.github.fintrack.workspace.member.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.workspace.member.controller.contract.MemberContract;
import io.github.fintrack.workspace.member.controller.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberContract contract;

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getById(
            @ValidUUID @PathVariable String id
    ) {
        return ResponseEntity.ok(contract.getById(id));
    }

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<List<MemberResponse>> getByWorkspaceId(
            @ValidUUID @PathVariable String workspaceId
    ) {
        return ResponseEntity.ok(contract.getByWorkspaceId(workspaceId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @ValidUUID @PathVariable String id
    ) {
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }
}
