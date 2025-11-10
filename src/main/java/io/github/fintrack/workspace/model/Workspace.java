package io.github.fintrack.workspace.model;

import io.github.fintrack._common.model.CreatedAndDeleteEntity;
import io.github.fintrack.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workspaces")
public class Workspace extends CreatedAndDeleteEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Builder
    public Workspace(String name, Type type, User createdBy) {
        this.name = name;
        this.type = type;
        this.getCreation().setCreatedBy(createdBy);
    }
}