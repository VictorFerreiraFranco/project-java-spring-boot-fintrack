package io.github.fintrack.workspace.financial.category.repository.specification;

import io.github.fintrack.transaction.model.Type;
import io.github.fintrack.workspace.financial.category.controller.dto.CategoryFilter;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    public static Specification<Category> applyFilter(CategoryFilter filter) {
        Specification<Category> spec = (root, query, cb) -> cb.conjunction();

        if (filter == null)
            return spec;

        if (filter.description() != null && !filter.description().isEmpty())
            spec = spec.and(descriptionLike(filter.description()));

        if (filter.color() != null && !filter.color().isEmpty())
            spec = spec.and(colorEqual(filter.color()));

        if (filter.type() != null && !filter.type().isEmpty())
            spec = spec.and(typeEqual(Type.valueOf(filter.type())));

        return spec;
    }

    public static Specification<Category> workspaceEqual(Workspace workspace){
        return (root, query, cb) -> cb.equal(root.get("workspace"), workspace);
    }

    public static Specification<Category> deletedAtIsNull() {
        return (root, query, cb) ->
                cb.isNull(root.get("deletion").get("deletedAt"));
    }

    public static Specification<Category> descriptionLike(String description) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Category> colorEqual(String color) {
        return (root, query, cb) -> cb.equal(root.get("color"), color);
    }

    public static Specification<Category> typeEqual(Type type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }
}
