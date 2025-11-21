package io.github.fintrack.transaction.entry.controller;

import io.github.fintrack.transaction.entry.controller.contract.EntryContract;
import io.github.fintrack.transaction.entry.controller.dto.EntryTransactionByMonthYearResponse;
import io.github.fintrack.transaction.entry.controller.dto.EntryTransactionByYearResponse;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entries")
@RequiredArgsConstructor
public class EntryController {

    private final EntryContract contract;

    @GetMapping("/workspace/{workspaceId}/month/{month}/year/{year}")
    public ResponseEntity<List<EntryTransactionByMonthYearResponse>> getByWorkspaceAndMonthAndYear(
            @PathVariable String workspaceId,
            @PathVariable Integer month,
            @PathVariable Integer year
    ) {
        return ResponseEntity.ok(contract.getByWorkspaceAndMonthAndYear(workspaceId, month, year));
    }

    @GetMapping("/workspace/{workspaceId}/year/{year}")
    public ResponseEntity<List<EntryTransactionByYearResponse>> getByWorkspaceAndYear(
            @PathVariable String workspaceId,
            @PathVariable Integer year
    ) {
        return ResponseEntity.ok(contract.getByWorkspaceAndYear(workspaceId, year));
    }
}
