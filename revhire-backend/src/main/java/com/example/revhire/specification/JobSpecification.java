package com.example.revhire.specification;

import com.example.revhire.entity.Job;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> filterJobs(
            String title,
            String location,
            Integer experienceYears,
            Double minSalary,
            Double maxSalary
    ) {

        return (root, query, criteriaBuilder) -> {

            var predicates = criteriaBuilder.conjunction();

            if (title != null && !title.isEmpty()) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        )
                );
            }

            if (location != null && !location.isEmpty()) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("location")),
                                "%" + location.toLowerCase() + "%"
                        )
                );
            }

            if (experienceYears != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("experienceYears"),
                                experienceYears
                        )
                );
            }

            if (minSalary != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("minSalary"),
                                minSalary
                        )
                );
            }

            if (maxSalary != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("maxSalary"),
                                maxSalary
                        )
                );
            }

            return predicates;
        };
    }
}