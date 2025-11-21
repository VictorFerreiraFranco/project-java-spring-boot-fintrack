package io.github.fintrack.transaction.installment.validator;

import io.github.fintrack.common.exception.DuplicateRecordException;
import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.installment.repository.InstallmentRepository;
import io.github.fintrack.transaction.transaction.exception.InvalidArgumentsTransactionException;
import io.github.fintrack.transaction.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InstallmentValidator {

    private final InstallmentRepository installmentRepository;

    public void validToSave(List<Installment> installments) {
        if (installmentExists(installments))
            throw new DuplicateRecordException("Installment already exists");
    }

    public void validTransactionToCreateInstallments(Transaction transaction) {
        if (transaction.getStartDate() == null)
            throw new InvalidArgumentsTransactionException("Transaction start date is required");

        if (transaction.getRecurrence().equals(Boolean.FALSE) && transaction.getEndDate() == null)
            throw new InvalidArgumentsTransactionException("Transaction end date is required");
    }

    public boolean installmentExists(List<Installment> installments) {
        if (installments.isEmpty())
            return false;

        List<Installment> mutableList = new ArrayList<>(installments);
        mutableList.sort(Comparator.comparing(Installment::getDate));

        List<Installment> installmentsListFund = installmentRepository.findAllByTransactionAndDateBetween(
                mutableList.getFirst().getTransaction(),
                mutableList.getFirst().getDate(),
                mutableList.getLast().getDate()
        );

        if (installmentsListFund.isEmpty())
            return false;

        for (Installment installmentFund : installmentsListFund) {

            boolean match = mutableList.stream().anyMatch(installment ->
                    (installment.getId() == null || !installment.getId().equals(installmentFund.getId()))
                            &&
                    installment.getDate().getMonthValue() == installmentFund.getDate().getMonthValue() &&
                    installment.getDate().getYear() == installmentFund.getDate().getYear()
            );

            if (match)
                return true;
        }

        return false;
    }
}
