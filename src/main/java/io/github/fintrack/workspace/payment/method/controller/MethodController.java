package io.github.fintrack.workspace.payment.method.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.workspace.payment.method.controller.contract.MethodContract;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodRequest;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodResponse;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments/methods")
@RequiredArgsConstructor
public class MethodController implements GenericController {

    private final MethodContract contract;

    @GetMapping("/{id}")
    public ResponseEntity<MethodResponse> getById(
            @ValidUUID @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(contract.getById(id));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<MethodResponse>> searchAllByWorkspace(
            @ValidUUID @PathVariable String workspaceId,
            @RequestBody MethodFilter filter
    ) {
        return ResponseEntity.ok(contract.searchAllByWorkspace(workspaceId, filter));
    }

    @GetMapping("/types")
    public ResponseEntity<List<MethodTypeResponse>> getTypes() {
        return ResponseEntity.ok(contract.getTypes());
    }

    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<MethodResponse> register(
            @ValidUUID @PathVariable("workspaceId") String workspaceId,
            @RequestBody MethodRequest request
    ) {
        MethodResponse methodResponse = contract.register(workspaceId, request);
        return ResponseEntity
                .created(buildHeaderLocation(methodResponse.id()))
                .body(methodResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MethodResponse> update(
            @ValidUUID @PathVariable("id") String id,
            @RequestBody MethodRequest request
    ) {
        return ResponseEntity.ok(contract.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @ValidUUID @PathVariable("id") String id
    ) {
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }
}
