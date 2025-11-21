package io.github.fintrack.transaction.entry.controller.contract;

import io.github.fintrack.transaction.entry.controller.dto.EntryTransactionByMonthYearResponse;
import io.github.fintrack.transaction.entry.controller.dto.EntryTransactionByYearResponse;
import io.github.fintrack.transaction.entry.controller.mapper.EntryMapper;
import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.transaction.service.TransactionService;
import io.github.fintrack.workspace.workspace.exception.WorkspaceNotFoundException;
import io.github.fintrack.workspace.workspace.model.Workspace;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class EntryContract {

    private final TransactionService transactionService;
    private final WorkspaceService workspaceService;
    private final WorkspaceValidator workspaceValidator;
    private final EntryMapper entryMapper;

    @Transactional()
    public List<EntryTransactionByMonthYearResponse> getByWorkspaceAndMonthAndYear(String workspaceId, Integer month, Integer year) {
        Workspace workspace = this.findWorkspaceById(workspaceId);

        transactionService.normalizeIrregularTransactionsByWorkspace(workspace);

        return transactionService.findAllByWorkspaceAndMonthAndYear(
                        workspace,
                        LocalDate.of(year, month, 1)
                )
                .stream()
                .map(transaction -> {
                    Installment installment = transaction.getInstallments().stream()
                            .filter(inst ->
                                    inst.getDate().getMonthValue() == month &&
                                    inst.getDate().getYear() == year
                            )
                            .findFirst()
                            .orElseThrow();

                    return entryMapper.toTransactionByMonthYearResponse(transaction, installment);
                })
                .toList();
    }

    @Transactional()
    public List<EntryTransactionByYearResponse> getByWorkspaceAndYear(String workspaceId, Integer year) {
        Workspace workspace = this.findWorkspaceById(workspaceId);

        transactionService.normalizeIrregularTransactionsByWorkspace(workspace);

        return transactionService.findAllByWorkspaceAndYear(
                        workspace,
                        LocalDate.of(year, 1, 1)
                )
                .stream()
                .map(transaction -> {
                    List<Installment> installments = transaction.getInstallments()
                            .stream()
                            .filter(inst -> inst.getDate().getYear() == year)
                            .toList();

                    return entryMapper.toTransactionByYearResponse(transaction, installments);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    protected Workspace findWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceService.findByIdAndDeletedAtIsNull(UUID.fromString(workspaceId))
                .orElseThrow(WorkspaceNotFoundException::new);

        workspaceValidator.validUserLoggedInIsMemberByWorkspace(workspace);

        return workspace;
    }

}
