package com.example.revhire.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.revhire.dto.CreateJobRequest;
import com.example.revhire.entity.Job;
import com.example.revhire.entity.User;
import com.example.revhire.enums.Role;
import com.example.revhire.repository.JobRepository;
import com.example.revhire.repository.UserRepository;
import com.example.revhire.specification.JobSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    

    public Job createJob(CreateJobRequest request, Authentication authentication) {

        User employer = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getRole() != Role.EMPLOYER) {
            throw new RuntimeException("Only employers can create jobs");
        }

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setExperienceYears(request.getExperienceYears());
        job.setRequiredEducation(request.getRequiredEducation());
        job.setLocation(request.getLocation());
        job.setSalaryMin(request.getMinSalary());
        job.setSalaryMax(request.getMaxSalary());
        job.setJobType(request.getJobType());
        job.setDeadline(request.getApplicationDeadline());
        job.setNumberOfOpenings(request.getNumberOfOpenings());

        job.setEmployer(employer);
        job.setIsActive(true);
        job.setIsFilled(false);

        return jobRepository.save(job);
    }
    
    public Page<Job> searchJobs(
            String title,
            String location,
            Integer experienceYears,
            Double minSalary,
            Double maxSalary,
            Pageable pageable
    ) {

        Specification<Job> specification = JobSpecification.filterJobs(
                title,
                location,
                experienceYears,
                minSalary,
                maxSalary
        );

        return jobRepository.findAll(specification, pageable);
    }
    
    public List<Job> getEmployerJobs(String email) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        return jobRepository.findAll()
                .stream()
                .filter(job -> job.getEmployer().getId().equals(employer.getId()))
                .toList();
    }
    
    public Job updateJob(String email, Long jobId, Job updatedJob) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new RuntimeException("Unauthorized action");
        }

        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setSkills(updatedJob.getSkills());
        job.setExperienceYears(updatedJob.getExperienceYears());
        job.setEducation(updatedJob.getEducation());
        job.setLocation(updatedJob.getLocation());
        job.setSalaryMin(updatedJob.getSalaryMin());
        job.setSalaryMax(updatedJob.getSalaryMax());
        job.setJobType(updatedJob.getJobType());
        job.setDeadline(updatedJob.getDeadline());
        job.setOpenings(updatedJob.getOpenings());

        return jobRepository.save(job);
    }
    
    private Job validateEmployerAccess(String email, Long jobId) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new RuntimeException("Unauthorized action");
        }

        return job;
    }
    public Job closeJob(String email, Long jobId) {

        Job job = validateEmployerAccess(email, jobId);

        job.setIsActive(false);

        return jobRepository.save(job);
    }
    
    public Job markAsFilled(String email, Long jobId) {

        Job job = validateEmployerAccess(email, jobId);

        job.setIsFilled(true);
        job.setIsActive(false);

        return jobRepository.save(job);
    }
    
    public void deleteJob(String email, Long jobId) {

        Job job = validateEmployerAccess(email, jobId);

        jobRepository.delete(job);
    }
    
    
}