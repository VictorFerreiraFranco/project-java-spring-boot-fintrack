package io.github.fintrack.workspace.invite.repository.specification;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.invite.model.Invite;
import io.github.fintrack.workspace.invite.model.Status;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.domain.Specification;

public class InviteSpecification {

    public static Specification<Invite> workspaceEqual(Workspace workspace) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("workspace"), workspace);
    }

    public static Specification<Invite> toEqual(User to) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("to"), to);
    }

    public static Specification<Invite> statusEqual(Status status) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
}
