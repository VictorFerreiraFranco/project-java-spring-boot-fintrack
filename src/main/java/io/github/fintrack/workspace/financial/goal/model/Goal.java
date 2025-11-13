package io.github.fintrack.workspace.financial.goal.model;

import io.github.fintrack.common.model.CreatedAndDeleteEntity;
import io.github.fintrack.workspace.financial.category.model.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "financial_category_goals")
public class Goal extends CreatedAndDeleteEntity {

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_category_id", nullable = false)
    private Category category;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;
}
