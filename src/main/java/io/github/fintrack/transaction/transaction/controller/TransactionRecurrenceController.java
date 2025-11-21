package io.github.fintrack.transaction.transaction.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.transaction.transaction.controller.contract.TransactionRecurrenceContract;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionRecurrenceRequest;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions/recurrence")
@RequiredArgsConstructor
public class TransactionRecurrenceController implements GenericController {

    private final TransactionRecurrenceContract contract;

    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<TransactionResponse> create(
            @ValidUUID @PathVariable String workspaceId,
            @Valid @RequestBody TransactionRecurrenceRequest request
    ) {
        TransactionResponse response = contract.create(workspaceId, request);
        return ResponseEntity
                .created(this.buildHeaderLocation(response.id()))
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @ValidUUID @PathVariable String id,
            @Valid @RequestBody TransactionRecurrenceRequest request
    )
    {
        return ResponseEntity.ok(contract.update(id, request));
    }
}
