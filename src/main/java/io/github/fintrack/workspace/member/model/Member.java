package io.github.fintrack.workspace.member.model;

import io.github.fintrack._common.model.CreatedAndDeleteEntity;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.workspace.model.Workspace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends CreatedAndDeleteEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;
}
