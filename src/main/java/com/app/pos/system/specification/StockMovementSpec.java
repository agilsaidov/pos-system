package com.app.pos.system.specification;

import com.app.pos.system.model.StockMovement;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockMovementSpec {

    public static Specification<StockMovement> withFilters(
            Long storeId,
            Long productId,
            OffsetDateTime from,
            OffsetDateTime to) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (storeId != null) {
                predicates.add(cb.equal(root.get("store").get("storeId"), storeId));
            }
            if (productId != null) {
                predicates.add(cb.equal(root.get("product").get("productId"), productId));
            }
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}