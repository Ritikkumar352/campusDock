package com.campusDock.campusdock.repository.specs;

import com.campusDock.campusdock.MarketPlace.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ProductSpecification {

    public static Specification<Product> withFilters(UUID collegeId, UUID userId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (collegeId != null) {
                predicates.add(cb.equal(root.get("college").get("id"), collegeId));
            }

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
