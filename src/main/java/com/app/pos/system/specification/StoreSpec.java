package com.app.pos.system.specification;

import com.app.pos.system.model.Store;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class StoreSpec {

    public static Specification<Store> withFilters(
            String name,
            String city,
            String address){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (name != null) predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            if (city != null) predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            if (address != null) predicates.add(cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
