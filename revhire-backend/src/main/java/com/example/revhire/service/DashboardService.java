package com.example.revhire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.revhire.dto.EmployerDashboardResponse;
import com.example.revhire.entity.User;
import com.example.revhire.enums.ApplicationStatus;
import com.example.revhire.repository.ApplicationRepository;
import com.example.revhire.repository.JobRepository;
import com.example.revhire.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public EmployerDashboardResponse getEmployerDashboard(String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

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
