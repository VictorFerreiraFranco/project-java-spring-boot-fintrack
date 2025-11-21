package io.github.fintrack.transaction.installment.controller.contrat;

import io.github.fintrack.transaction.installment.controller.dto.InstallmentPreviewRequest;
import io.github.fintrack.transaction.installment.controller.dto.InstallmentResponse;
import io.github.fintrack.transaction.installment.controller.mapper.InstallmentMapper;
import io.github.fintrack.transaction.installment.service.InstallmentService;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstallmentContract {

    private final InstallmentService installmentService;
    private final InstallmentMapper installmentMapper;
    private final TransactionService transactionService;

    public List<InstallmentResponse> preview(InstallmentPreviewRequest request) {
        Transaction transactionTemporary = installmentMapper.toTransactionTemporary(request);
        transactionService.defineEndDate(transactionTemporary);

        return installmentService.getOrCreateInstallmentsByTransaction(transactionTemporary)
                .stream()
                .map(installmentMapper::toPreviewResponse)
                .toList();

    }
}
