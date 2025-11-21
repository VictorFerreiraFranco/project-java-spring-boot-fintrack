package io.github.fintrack.workspace.payment.method.model;

import io.github.fintrack.common.model.CreatedAndDeleteEntity;
import io.github.fintrack.workspace.workspace.model.Workspace;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_methods")
public class Method extends CreatedAndDeleteEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Workspace workspace;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
}
