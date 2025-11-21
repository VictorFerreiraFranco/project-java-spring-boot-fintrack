package io.github.fintrack.transaction.transaction.controller.contract;

import io.github.fintrack.transaction.transaction.controller.dto.TransactionRequest;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionResponse;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionTypeResponse;
import io.github.fintrack.transaction.transaction.controller.mapper.TransactionMapper;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.transaction.transaction.service.TransactionService;
import io.github.fintrack.workspace.financial.category.service.CategoryService;
import io.github.fintrack.workspace.payment.method.service.MethodService;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import io.github.fintrack.workspace.workspace.service.validator.WorkspaceValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class TransactionContract extends TransactionContractBase {

    private final TransactionMapper transactionMapper;

    public TransactionContract(
            WorkspaceService workspaceService,
            WorkspaceValidator workspaceValidator,
            TransactionService transactionService,
            CategoryService categoryService,
            MethodService methodService,
            TransactionMapper transactionMapper
    ) {
        super(workspaceService, workspaceValidator, transactionService, categoryService, methodService, transactionMapper);
        this.transactionMapper = transactionMapper;
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(String id) {
        return transactionMapper.toResponse(this.findById(id));
    }

//    @Transactional()
//    public List<TransactionResponse> getByWorkspaceAndMonthAndYear(String workspaceId, Integer month, Integer year) {
//        Workspace workspace = this.findWorkspaceById(workspaceId);
//
//        transactionService.normalizeIrregularTransactionsByWorkspace(workspace);
//
//        return transactionService.findAllByWorkspaceAndMonthAndYear(
//                        workspace,
//                        LocalDate.of(year, month, 1)
//                )
//                .stream()
//                .map(transactionMapper::toResponse)
//                .toList();
//    }
//
//    @Transactional()
//    public List<TransactionResponse> getByWorkspaceAndYear(String workspaceId, Integer year) {
//        Workspace workspace = this.findWorkspaceById(workspaceId);
//
//        transactionService.normalizeIrregularTransactionsByWorkspace(workspace);
//
//        return transactionService.findAllByWorkspaceAndYear(
//                        workspace,
//                        LocalDate.of(year, 1, 1)
//                )
//                .stream()
//                .map(transactionMapper::toResponse)
//                .toList();
//    }

    public List<TransactionTypeResponse> getType() {
        return Arrays.stream(Type.values())
                .map(type -> new TransactionTypeResponse(type, type.getDescription()))
                .toList();
    }

    @Transactional
    public TransactionResponse create(String workspaceId, TransactionRequest request) {
        return super.create(
                transactionMapper.toTransactionByRequest(request),
                workspaceId,
                request.categoryId(),
                request.methodId()
        );
    }

    @Transactional
    public TransactionResponse update(String id, TransactionRequest request) {
        Transaction transaction = this.findById(id);
        transactionMapper.updateEntityByRequest(transaction, request);

        return super.update(
                transaction,
                request.categoryId(),
                request.methodId()
        );
    }

    @Transactional
    public void delete(String id) {
        transactionService.delete(this.findById(id));
    }
}
