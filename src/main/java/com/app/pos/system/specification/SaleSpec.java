package com.app.pos.system.specification;

import com.app.pos.system.model.Sale;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class SaleSpec {

    public static Specification<Sale> withFilters(
            Long saleId,
            Long cashierId,
            Long storeId,
            OffsetDateTime from,
            OffsetDateTime to){

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(saleId != null) predicates.add(criteriaBuilder.equal(root.get("saleId"), saleId));
            if(cashierId != null) predicates.add(criteriaBuilder.equal(root.get("cashier").get("userId"), cashierId));
            if(storeId != null) predicates.add(criteriaBuilder.equal(root.get("store").get("storeId"), storeId));
            if(from != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), from));
            if(to != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), to));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
