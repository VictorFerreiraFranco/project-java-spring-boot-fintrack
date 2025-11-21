package io.github.fintrack.workspace.payment.method.repository.specification;

import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.model.Type;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.domain.Specification;

public class MethodSpecification {
    public static Specification<Method> applyFilter (MethodFilter filter) {
        Specification<Method> spec = (root, query, cb) -> cb.conjunction();

        if (filter ==  null) return spec;

        if (filter.description() != null && !filter.description().isEmpty())
            spec = spec.and(descriptionLike(filter.description()));

        if (filter.type() != null && !filter.type().isEmpty())
            spec = spec.and(typeEqual(Type.valueOf(filter.type())));

        return spec;
    }

    public static Specification<Method> workspaceEqual(Workspace workspace) {
        return (root, query, cb) ->
                cb.equal(root.get("workspace"), workspace);
    }

    public static Specification<Method> deletedAtIsNull() {
        return (root, query, cb) ->
                cb.isNull(root.get("deletion").get("deletedAt"));
    }

    public static Specification<Method> descriptionLike(String description) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Method> typeEqual(Type type) {
        return (root, query, cb) ->
                cb.equal(root.get("type"), type);
    }
}
