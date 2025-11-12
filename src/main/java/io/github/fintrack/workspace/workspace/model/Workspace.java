package io.github.fintrack.workspace.workspace.model;

import io.github.fintrack.common.model.CreatedAndDeleteEntity;
import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "workspace", fetch = FetchType.LAZY)
    private List<Member> members;

    @Builder
    public Workspace(String name, Type type, User createdBy) {
        this.name = name;
        this.type = type;
        this.getCreation().setCreatedBy(createdBy);
    }

    public Member getOwner() {
        return members.stream()
                .filter(Member::isOwner)
                .findFirst()
                .orElse(null);
    }

    public boolean isOwner(Member member) {}
}