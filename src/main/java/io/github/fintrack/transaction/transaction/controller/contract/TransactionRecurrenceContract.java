package io.github.fintrack.transaction.transaction.controller.contract;

import io.github.fintrack.transaction.transaction.controller.dto.TransactionRecurrenceRequest;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionResponse;
import io.github.fintrack.transaction.transaction.controller.mapper.TransactionMapper;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.service.TransactionService;
import io.github.fintrack.workspace.financial.category.service.CategoryService;
import io.github.fintrack.workspace.payment.method.service.MethodService;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class TransactionRecurrenceContract extends TransactionContractBase {

    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    public TransactionRecurrenceContract(
            WorkspaceService workspaceService,
            TransactionService transactionService,
            CategoryService categoryService,
            MethodService methodService,
            TransactionMapper transactionMapper
    ) {
        super(workspaceService, transactionService, categoryService, methodService, transactionMapper);
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
    }

    @Transactional
    public TransactionResponse create(String workspaceId, TransactionRecurrenceRequest request) {
        return super.create(
                transactionMapper.toTransactionByRequestRecurrence(request),
                workspaceId,
                request.categoryId(),
                request.methodId()
        );
    }

    @Transactional
    public TransactionResponse update(String id, TransactionRecurrenceRequest request) {
        Transaction transaction = transactionService.findByIdAndValidateExistenceAndMembership(UUID.fromString(id));
        transactionMapper.updateEntityByRequestRecurrence(transaction, request);

        return super.update(
                transaction,
                request.categoryId(),
                request.methodId()
        );
    }
}
