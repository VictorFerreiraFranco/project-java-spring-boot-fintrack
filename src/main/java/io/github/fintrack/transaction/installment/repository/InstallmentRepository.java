package io.github.fintrack.transaction.installment.repository;

import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InstallmentRepository extends JpaRepository<Installment, UUID>, JpaSpecificationExecutor<Installment> {

    List<Installment> findAllByTransactionAndDateBetween(Transaction transaction, LocalDate dateAfter, LocalDate dateBefore);
}
