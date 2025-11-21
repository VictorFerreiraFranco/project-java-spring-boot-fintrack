package io.github.fintrack.transaction.transaction.validator;

import io.github.fintrack.transaction.transaction.exception.InvalidArgumentsTransactionException;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.repository.TransactionRepository;
import io.github.fintrack.workspace.workspace.validator.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

    private final WorkspaceValidator workspaceValidator;

    public void validaToSave(Transaction transaction){
        if (transaction.getStartDate() == null)
            throw new InvalidArgumentsTransactionException("Transaction start date is required");

        if (transaction.getRecurrence().equals(Boolean.TRUE) && transaction.getAmountInstallment() == null)
            throw new InvalidArgumentsTransactionException("Amount installment is required for fixed transaction");

        if (transaction.getRecurrence().equals(Boolean.FALSE) && transaction.getAmount() == null)
            throw new InvalidArgumentsTransactionException("Amount is required");

        if (transaction.getRecurrence().equals(Boolean.FALSE)
                && (transaction.getTotalInstallment() == null || transaction.getTotalInstallment() <= 0))
            throw new InvalidArgumentsTransactionException("Total installment is required");
    }

    public void validToDelete(Transaction transaction){
        workspaceValidator.validUserLoggedInIsMemberByWorkspace(transaction.getWorkspace());
    }
}
