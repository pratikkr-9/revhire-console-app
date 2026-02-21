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