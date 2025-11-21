package io.github.fintrack.transaction.transaction.controller;

import io.github.fintrack.common.annotation.validator.uuid.ValidUUID;
import io.github.fintrack.common.controller.GenericController;
import io.github.fintrack.transaction.transaction.controller.contract.TransactionContract;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionRequest;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionResponse;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController implements GenericController {

    private final TransactionContract contract;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(
            @ValidUUID @PathVariable String id
    ) {
        return ResponseEntity.ok(contract.getById(id));
    }

    @GetMapping("/types")
    public ResponseEntity<List<TransactionTypeResponse>> getType()
    {
        return ResponseEntity.ok(contract.getType());
    }

    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<TransactionResponse> create(
            @ValidUUID @PathVariable String workspaceId,
            @Valid @RequestBody TransactionRequest request
    ) {
        TransactionResponse response = contract.create(workspaceId, request);
        return ResponseEntity
                .created(this.buildHeaderLocation(response.id()))
                .body(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @ValidUUID @PathVariable String id,
            @Valid @RequestBody TransactionRequest request
    )
    {
        return ResponseEntity.ok(contract.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @ValidUUID @PathVariable String id
    ) {
        contract.delete(id);
        return ResponseEntity.noContent().build();
    }
}
