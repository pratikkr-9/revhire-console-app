package com.example.revhire.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.revhire.dto.EmployerDashboardResponse;
import com.example.revhire.entity.User;
import com.example.revhire.enums.ApplicationStatus;
import com.example.revhire.enums.Role;
import com.example.revhire.repository.ApplicationRepository;
import com.example.revhire.repository.JobRepository;
import com.example.revhire.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public EmployerDashboardResponse getEmployerDashboard(Authentication authentication) {

        User employer = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getRole() != Role.EMPLOYER) {
            throw new RuntimeException("Only employers can access dashboard");
        }

        long totalJobs = jobRepository.countByEmployerId(employer.getId());
        long activeJobs = jobRepository.countByEmployerIdAndIsActiveTrue(employer.getId());
        long filledJobs = jobRepository.countByEmployerIdAndIsFilledTrue(employer.getId());

        long totalApplications =
                applicationRepository.countByJobEmployerId(employer.getId());

        long pendingReviews =
                applicationRepository.countByJobEmployerIdAndStatus(
                        employer.getId(),
                        ApplicationStatus.APPLIED);

        return EmployerDashboardResponse.builder()
                .totalJobs(totalJobs)
                .activeJobs(activeJobs)
                .filledJobs(filledJobs)
                .totalApplications(totalApplications)
                .pendingReviews(pendingReviews)
                .build();
    }
    
    
    
}
