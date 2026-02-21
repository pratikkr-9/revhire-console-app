package com.example.revhire.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.revhire.entity.Job;
import com.example.revhire.entity.User;
import com.example.revhire.repository.JobRepository;
import com.example.revhire.repository.UserRepository;
import com.example.revhire.specification.JobSpecification;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public Job createJob(String email, Job job) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        job.setEmployer(employer);
        job.setPostedDate(LocalDateTime.now());
        job.setIsActive(true);

        return jobRepository.save(job);
    }

    public List<Job> searchJobs(String title,
                                 String location,
                                 Integer experience,
                                 Double minSalary,
                                 Double maxSalary) {

        Specification<Job> spec = Specification
                .where(JobSpecification.hasTitle(title))
                .and(JobSpecification.hasLocation(location))
                .and(JobSpecification.hasExperience(experience))
                .and(JobSpecification.hasSalaryRange(minSalary, maxSalary));

        return jobRepository.findAll(spec);
    }
}