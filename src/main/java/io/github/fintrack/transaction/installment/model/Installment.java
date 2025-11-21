package io.github.fintrack.transaction.installment.model;

import io.github.fintrack.common.model.BaseEntity;
import io.github.fintrack.transaction.transaction.model.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "installments")
public class Installment extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean wasPaid;

    @Column(nullable = false)
    private Integer installmentNumber;
}
