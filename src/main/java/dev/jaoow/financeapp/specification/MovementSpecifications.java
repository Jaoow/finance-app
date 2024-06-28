package dev.jaoow.financeapp.specification;

import dev.jaoow.financeapp.entity.Movement;
import dev.jaoow.financeapp.model.MovementType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MovementSpecifications {

    public static Specification<Movement> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Movement> hasType(MovementType type) {
        return (root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Movement> hasDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("date"), startDate, endDate);
            }

            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate);
            }
        };
    }

    public static Specification<Movement> hasAccount(Long accountId) {
        return (root, query, criteriaBuilder) ->
                accountId == null ? null : criteriaBuilder.equal(root.get("account").get("id"), accountId);
    }
}
