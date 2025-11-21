package io.github.fintrack.transaction.transaction.model;

import io.github.fintrack.common.model.CreatedAndDeleteEntity;
import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.workspace.model.Workspace;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction extends CreatedAndDeleteEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private Method method;

    @Column(nullable = false)
    private String description;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "amount_installment", precision = 15, scale = 2)
    private BigDecimal amountInstallment;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_installment", nullable = false)
    private Integer totalInstallment;

    @Column(nullable = false)
    private Boolean recurrence;

    @Column()
    private String observation;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
    private List<Installment> installments;
}
