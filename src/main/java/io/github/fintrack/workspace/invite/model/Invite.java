package io.github.fintrack.workspace.invite.model;

import io.github.fintrack.common.model.CreatedEntity;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.workspace.model.Workspace;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workspace_invites")
public class Invite extends CreatedEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public boolean isPending() {
        return Status.PENDING.equals(status);
    }
}
