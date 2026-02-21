package com.example.revhire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.revhire.dto.ApplyRequest;
import com.example.revhire.entity.Application;
import com.example.revhire.entity.Job;
import com.example.revhire.entity.User;
import com.example.revhire.enums.ApplicationStatus;
import com.example.revhire.repository.ApplicationRepository;
import com.example.revhire.repository.JobRepository;
import com.example.revhire.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public Application applyForJob(String email, ApplyRequest request) {

        User applicant = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getIsActive()) {
            throw new RuntimeException("Job is not active");
        }

        if (applicationRepository.findByJobAndApplicant(job, applicant).isPresent()) {
            throw new RuntimeException("Already applied to this job");
        }

        Application application = Application.builder()
                .job(job)
                .applicant(applicant)
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.APPLIED)
                .appliedDate(LocalDateTime.now())
                .build();

        return applicationRepository.save(application);
    }

    public Application withdrawApplication(String email, Long applicationId, String reason) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getApplicant().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized action");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setWithdrawReason(reason);

        return applicationRepository.save(application);
    }

    public List<Application> viewMyApplications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository.findByApplicant(user);
    }
}
