package io.github.fintrack.transaction.transaction.service;

import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.installment.service.InstallmentService;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.transaction.transaction.repository.TransactionRepository;
import io.github.fintrack.transaction.transaction.repository.specification.TransactionSpecification;
import io.github.fintrack.transaction.transaction.validator.TransactionValidator;
import io.github.fintrack.workspace.workspace.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;
    private final InstallmentService installmentService;

    public Optional<Transaction> findByIdAndDeleteAtIsNull(UUID id) {
        return transactionRepository.findByIdAndDeletionDeletedAtIsNull(id);
    }

    public List<Transaction> findAllByWorkspaceAndMonthAndYear(Workspace workspace, LocalDate date){
        return transactionRepository.findAll(
                TransactionSpecification.deletedAtIsNull()
                        .and(TransactionSpecification.workspaceEqual(workspace))
                        .and(TransactionSpecification.withInstallmentsInMonthAndYear(date))
        );
    }

    public List<Transaction> findAllByWorkspaceAndYear(Workspace workspace, LocalDate date){
        return transactionRepository.findAll(
                TransactionSpecification.deletedAtIsNull()
                        .and(TransactionSpecification.workspaceEqual(workspace))
                        .and(TransactionSpecification.withInstallmentsInYear(date))
        );
    }

    @Transactional
    public void save(Transaction transaction){
        transactionValidator.validaToSave(transaction);

        this.defineEndDate(transaction);

        if (transaction.getId() == null) {
            this.defineTotalInstallment(transaction);
            this.defineAmount(transaction);

            transactionRepository.save(transaction);
        }

        installmentService.normalizedInstallmentsByTransaction(transaction);

        this.defineTotalInstallment(transaction);
        this.defineAmount(transaction);

        transactionRepository.save(transaction);
    }

    @Transactional
    public void delete(Transaction transaction){
        transactionValidator.validToDelete(transaction);
        installmentService.deleteAll(transaction.getInstallments());
        transactionRepository.delete(transaction);
    }

    @Transactional
    public void normalizeIrregularTransactionsByWorkspace(Workspace workspace){
        LocalDate now = LocalDate.now();
        transactionRepository.findAll(
                TransactionSpecification.deletedAtIsNull()
                        .and(TransactionSpecification.workspaceEqual(workspace))
                        .and(TransactionSpecification.endDateIsNull())
                        .and(TransactionSpecification.startDateThanOrEqual(now))
                        .and(TransactionSpecification.withoutInstallmentsInMonthAndYear(now))
        ).forEach(installmentService::normalizedInstallmentsByTransaction);
    }

    public void defineEndDate(Transaction transaction){
        if (transaction.getRecurrence().equals(Boolean.TRUE))
            return;

        Integer totalInstallment = transaction.getTotalInstallment();
        totalInstallment--;

        transaction.setEndDate(
                transaction.getStartDate()
                        .plusMonths(totalInstallment)
                        .withDayOfMonth(
                                Math.min(
                                        transaction.getStartDate().getDayOfMonth(),
                                        transaction.getStartDate().plusMonths(
                                                totalInstallment
                                        ).lengthOfMonth()
                                )
                        )
        );
    }

    protected void defineTotalInstallment(Transaction transaction){
        if (transaction.getRecurrence().equals(Boolean.FALSE))
            return;

        transaction.setTotalInstallment(transaction.getInstallments().size());
    }

    protected void defineAmount(Transaction transaction){
        if (transaction.getRecurrence().equals(Boolean.FALSE))
            return;

        transaction.setAmount(transaction.getInstallments()
                .stream()
                .map(Installment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}
