package com.example.revhire.specification;



import org.springframework.data.jpa.domain.Specification;

import com.example.revhire.entity.Job;

public class JobSpecification {

    public static Specification<Job> hasTitle(String title) {
        return (root, query, cb) ->
                title == null ? null :
                        cb.like(cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%");
    }

    public static Specification<Job> hasLocation(String location) {
        return (root, query, cb) ->
                location == null ? null :
                        cb.like(cb.lower(root.get("location")),
                                "%" + location.toLowerCase() + "%");
    }

    public static Specification<Job> hasExperience(Integer exp) {
        return (root, query, cb) ->
                exp == null ? null :
                        cb.lessThanOrEqualTo(root.get("experienceYears"), exp);
    }

    public static Specification<Job> hasSalaryRange(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null) {
                return cb.between(root.get("salaryMin"), min, max);
            }

            return null;
        };
    }
}