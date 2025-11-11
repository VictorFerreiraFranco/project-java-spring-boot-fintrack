package io.github.fintrack.workspace.member.model;

import io.github.fintrack.common.model.CreatedAndDeleteEntity;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.workspace.model.Workspace;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workspace_members")
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

    public boolean isOwner() {
        return Role.OWNER.equals(this.getRole());
    }

    public boolean isMember() {
        return Role.MEMBER.equals(this.getRole());
    }

    public boolean isViewer() {
        return Role.VIEWER.equals(this.getRole());
    }
}
