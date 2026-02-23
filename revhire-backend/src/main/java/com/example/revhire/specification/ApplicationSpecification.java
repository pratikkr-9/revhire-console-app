package com.example.revhire.specification;

import com.example.revhire.entity.Application;
import com.example.revhire.entity.User;
import com.example.revhire.enums.ApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import java.time.LocalDateTime;

public class ApplicationSpecification {

    public static Specification<Application> filterApplicants(
            Long jobId,
            ApplicationStatus status,
            LocalDateTime appliedAfter,
            Integer experienceYears,
            String skills,
            String education
    ) {

        return (root, query, cb) -> {

            var predicates = cb.conjunction();

            // Filter by Job
            if (jobId != null) {
                predicates = cb.and(
                        predicates,
                        cb.equal(root.get("job").get("id"), jobId)
                );
            }

            // Filter by Status
            if (status != null) {
                predicates = cb.and(
                        predicates,
                        cb.equal(root.get("status"), status)
                );
            }

            // Filter by Applied Date
            if (appliedAfter != null) {
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(
                                root.get("appliedDate"),
                                appliedAfter
                        )
                );
            }

            // Join with Applicant
            Join<Application, User> applicantJoin = root.join("applicant");

            // Filter by Experience
            if (experienceYears != null) {
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(
                                applicantJoin.get("experienceYears"),
                                experienceYears
                        )
                );
            }

            // Filter by Skills
            if (skills != null && !skills.isEmpty()) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(applicantJoin.get("skills")),
                                "%" + skills.toLowerCase() + "%"
                        )
                );
            }

            // Filter by Education
            if (education != null && !education.isEmpty()) {
                predicates = cb.and(
                        predicates,
                        cb.like(
                                cb.lower(applicantJoin.get("education")),
                                "%" + education.toLowerCase() + "%"
                        )
                );
            }

            return predicates;
        };
    }
}