package com.app.pos.system.specification;

import com.app.pos.system.model.User;
import com.app.pos.system.model.enums.RoleName;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class UserSpec {
    public static Specification<User> withFilters(Long userId, String search, RoleName role, Boolean enabled){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(userId != null){
                predicates.add(cb.equal(root.get("userId"), userId));
            }
            if(search != null){
                predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + search.toLowerCase() + "%"));
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + search.toLowerCase() + "%"));
            }
            if (enabled != null) {
                predicates.add(cb.equal(root.get("enabled"), enabled));
            }
            if(role != null && role != RoleName.ADMIN){
                predicates.add(cb.equal(root.join("userRoles").get("role").get("roleName"),role));
            }
            else{
                predicates.add(cb.notEqual(root.join("userRoles").get("role").get("roleName"), RoleName.ADMIN));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
