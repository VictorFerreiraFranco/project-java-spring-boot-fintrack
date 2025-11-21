package io.github.fintrack.workspace.member.repository.specification;

import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpecification {

    public static Specification<Member> workspaceEqual(Workspace workspace) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("workspace"), workspace);
    }

    public static Specification<Member> roleEqual(Role role) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), role);
    }

    public static Specification<Member> deletedAtIsNull() {
        return (root, query, cb) ->
                cb.isNull(root.get("deletion").get("deletedAt"));
    }
}
