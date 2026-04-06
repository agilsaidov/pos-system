package com.app.pos.system.specification;

import com.app.pos.system.model.Promotion;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class PromotionSpec {
    public static Specification<Promotion> withFilter(
            String name, Boolean active,
            OffsetDateTime startsAt, OffsetDateTime endsAt) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank())
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            if (active != null)
                predicates.add(cb.equal(root.get("active"), active));
            if (startsAt != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("startsAt"), startsAt));
            if (endsAt != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("endsAt"), endsAt));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
