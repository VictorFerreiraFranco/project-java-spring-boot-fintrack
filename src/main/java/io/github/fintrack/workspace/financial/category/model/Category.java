package io.github.fintrack.workspace.financial.category.model;

import io.github.fintrack.common.model.CreatedAndDeleteEntity;
import io.github.fintrack.transaction.transaction.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "financial_categories")
public class Category extends CreatedAndDeleteEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Workspace workspace;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String color;
}
