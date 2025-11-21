package io.github.fintrack.workspace.workspace.repository.specification;

import io.github.fintrack.auth.model.User;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class WorkspaceSpecification {

    public static Specification<Workspace> membersUserEqual(User user) {
        return (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("members").get("user"), user);
    }

    public static Specification<Workspace> deletedAtIsNull() {
        return (root, query, cb) ->
                cb.isNull(root.get("deletion").get("deletedAt"));
    }
}
