package io.github.fintrack.transaction.installment.service;

import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.installment.repository.InstallmentRepository;
import io.github.fintrack.transaction.installment.service.validator.InstallmentValidator;
import io.github.fintrack.transaction.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final InstallmentValidator installmentValidator;

    @Transactional
    public void saveAll(List<Installment> installments) {
        installmentValidator.validToSave(installments);
        installmentRepository.saveAll(installments);
    }

    public void deleteAll(List<Installment> installments) {
        installmentRepository.deleteAll(installments);
    }

    @Transactional
    public void normalizedInstallmentsByTransaction(Transaction transaction) {
        List<Installment> installments = this.getOrCreateInstallmentsByTransaction(transaction);
        transaction.setInstallments(installments);

        Predicate<Installment> outsidePeriod = buildOutsidePeriodPredicate(transaction);

        List<Installment> toDelete = installments.stream()
                .filter(outsidePeriod)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Installment> toKeep = installments.stream()
                .filter(outsidePeriod.negate())
                .collect(Collectors.toCollection(ArrayList::new));

        this.deleteAll(toDelete);

        transaction.setInstallments(toKeep);
        this.saveAll(toKeep);
    }

    public List<Installment> getOrCreateInstallmentsByTransaction(Transaction transaction) {
        installmentValidator.validTransactionToCreateInstallments(transaction);

        List<Installment> installments = new ArrayList<>();

        LocalDate startDate = transaction.getStartDate();
        LocalDate endDate = (transaction.getEndDate() != null) ? transaction.getEndDate() : LocalDate.now();

        LocalDate current = startDate;

        while (!current.withDayOfMonth(1).isAfter(endDate.withDayOfMonth(1))) {

            Installment installment = this.searchOrCreateInstallmentByTransaction(
                    transaction, current
            );

            installment.setInstallmentNumber(installments.size() + 1);

            this.defineAmountOfInstallment(transaction, installment);

            installments.add(installment);

            current = current.plusMonths(1).withDayOfMonth(
                    Math.min(startDate.getDayOfMonth(), current.plusMonths(1).lengthOfMonth())
            );
        }

        return installments;
    }

    private Installment searchOrCreateInstallmentByTransaction(
            Transaction transaction,
            LocalDate date
    ) {
        return transaction.getInstallments()
                .stream()
                .filter(installmentFilter ->
                        installmentFilter.getDate().getMonthValue() == date.getMonthValue() &&
                                installmentFilter.getDate().getYear() == date.getYear()
                )
                .findFirst()
                .orElse(Installment.builder()
                        .transaction(transaction)
                        .date(date)
                        .wasPaid(false)
                        .build()
                );
    }

    private void defineAmountOfInstallment(
            Transaction transaction, Installment installment
    ) {
        if (transaction.getRecurrence().equals(Boolean.TRUE)) {
            installment.setAmount(transaction.getAmountInstallment());
            return;
        }

        BigDecimal amount = transaction.getAmount().divide(
                new BigDecimal(transaction.getTotalInstallment()),
                2,
                RoundingMode.DOWN
        );

        if (installment.getInstallmentNumber() == 1)
            amount = amount.add(
                    transaction.getAmount().subtract(
                            amount.multiply(BigDecimal.valueOf(transaction.getTotalInstallment()))
                    )
            );

        installment.setAmount(amount);
    }

    private Predicate<Installment> buildOutsidePeriodPredicate(Transaction transaction) {

        LocalDate start = transaction.getStartDate().withDayOfMonth(1);
        LocalDate end = (transaction.getEndDate() != null
                ? transaction.getEndDate()
                : LocalDate.now()
        ).withDayOfMonth(1);

        return installment -> {
            LocalDate installmentDate = installment.getDate().withDayOfMonth(1);

            return installmentDate.isBefore(start) || installmentDate.isAfter(end);
        };
    }
}
