package io.github.fintrack.transaction.transaction.repository.specification;

import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.workspace.workspace.model.Workspace;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TransactionSpecification {

    public static Specification<Transaction> deletedAtIsNull() {
        return (root, query, cb) ->
                cb.isNull(root.get("deletion").get("deletedAt"));
    }

    public static Specification<Transaction> endDateIsNull() {
        return (root, query, cb) ->
                cb.isNull(root.get("endDate"));
    }

    public static Specification<Transaction> workspaceEqual(Workspace workspace) {
        return (root, query, cb) ->
                cb.equal(root.get("workspace"), workspace);
    }

    public static Specification<Transaction> startDateThanOrEqual(LocalDate startDate) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    public static Specification<Transaction> withoutInstallmentsInMonthAndYear(LocalDate date) {
        return (root, query, cb) -> {

            Join<Transaction, Installment> join =
                    joinInstallmentsByMonthAndYear(root, cb, date);

            return cb.isNull(join.get("id"));
        };
    }

    public static Specification<Transaction> withInstallmentsInMonthAndYear(LocalDate date) {
        return (root, query, cb) -> {

            Join<Transaction, Installment> join =
                    joinInstallmentsByMonthAndYear(root, cb, date);

            return cb.isNotNull(join.get("id"));
        };
    }

    public static Specification<Transaction> withInstallmentsInYear(LocalDate date) {
        return (root, query, cb) -> {

            Join<Transaction, Installment> join =
                    joinInstallmentsByYear(root, cb, date);

            return cb.isNotNull(join.get("id"));
        };
    }

    private static Join<Transaction, Installment> joinInstallmentsByYear(
            Root<Transaction> root,
            CriteriaBuilder cb,
            LocalDate date
    ) {

        Join<Transaction, Installment> join =
                root.join("installments", JoinType.LEFT);

        Predicate yearMatch = cb.equal(
                cb.function("date_part", Integer.class,
                        cb.literal("year"), join.get("date")),
                date.getYear()
        );

        join.on(cb.and(yearMatch));

        return join;
    }

    private static Join<Transaction, Installment> joinInstallmentsByMonthAndYear(
            Root<Transaction> root,
            CriteriaBuilder cb,
            LocalDate date
    ) {

        Join<Transaction, Installment> join = joinInstallmentsByYear(root, cb, date);

        Predicate monthMatch = cb.equal(
                cb.function("date_part", Integer.class,
                        cb.literal("month"), join.get("date")),
                date.getMonthValue()
        );

        join.on(cb.and(monthMatch));

        return join;
    }
}
