package io.github.fintrack.workspace.invite.controller;

import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.workspace.invite.controller.contract.InviteContract;
import io.github.fintrack.workspace.invite.controller.dto.InviteRequest;
import io.github.fintrack.workspace.invite.controller.dto.InviteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController implements GenericController {

    private final InviteContract contract;

    @GetMapping("{id}")
    public ResponseEntity<InviteResponse> findById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(contract.findById(id));
    }

    @GetMapping("pending/workspace/{workspaceId}")
    public ResponseEntity<List<InviteResponse>> findAllIsPendingByWorkspace(
            @PathVariable String workspaceId
    ) {
        return ResponseEntity.ok(contract.findAllIsPendingByWorkspace(workspaceId));
    }

    @GetMapping("pending/my")
    public ResponseEntity<List<InviteResponse>> findAllIsPendingByUserLoggedIn() {
        return ResponseEntity.ok(contract.findAllIsPendingByUserLoggedIn());
    }

    @PostMapping
    public ResponseEntity<InviteResponse> register(
            @Valid @RequestBody InviteRequest request
    ) {
        InviteResponse inviteResponse = contract.register(request);
        return ResponseEntity
                .created(this.buildHeaderLocation(inviteResponse.id()))
                .body(inviteResponse);
    }

    @PutMapping("accept/{id}")
    public ResponseEntity<Void> accept(
            @PathVariable String id
    ) {
        contract.accept(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("refused/{id}")
    public ResponseEntity<Void> refused(
            @PathVariable String id
    ) {
        contract.refused(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<Void> canceled(
            @PathVariable String id
    ) {
        contract.canceled(id);
        return ResponseEntity.noContent().build();
    }
}
