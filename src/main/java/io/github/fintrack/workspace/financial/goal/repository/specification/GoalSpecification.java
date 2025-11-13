package io.github.fintrack.workspace.financial.goal.repository.specification;

import io.github.fintrack.workspace.financial.goal.controller.dto.GoalFilter;
import io.github.fintrack.workspace.financial.goal.model.Goal;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class GoalSpecification {

    public static Specification<Goal> applyFilter(GoalFilter filter) {
        Specification<Goal> spec = (root, query, cb) -> cb.conjunction();

        if (filter == null)
            return spec;

        if (filter.description() != null && !filter.description().isEmpty())
            spec = spec.and(descriptionLike(filter.description()));

        if (filter.amount() != null)
            spec = spec.and(amountEquals(filter.amount()));

        if (filter.categoryId() != null && !filter.categoryId().isEmpty())
            spec = spec.and(categoryEqual(UUID.fromString(filter.categoryId())));

        return spec;
    }

    public static Specification<Goal> deletedAtIsNull() {
        return (root, query, cb) ->
                cb.isNull(root.get("deletion").get("deletedAt"));
    }

    public static Specification<Goal> descriptionLike(String description) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Goal> amountEquals(BigDecimal amount) {
        return (root, query, cb)
                -> cb.equal(root.get("amount"), amount);
    }

    public static Specification<Goal> categoryEqual(UUID categoryId) {
        return (root, query, cb) ->
                cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Goal> workspaceEqual(Workspace workspace) {
        return (root, query, cb) ->
                cb.equal(root.get("category").get("workspace"), workspace);
    }
}
