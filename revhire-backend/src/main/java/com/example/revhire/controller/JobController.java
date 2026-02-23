package com.example.revhire.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.dto.CreateJobRequest;
import com.example.revhire.entity.Job;
import com.example.revhire.service.JobService;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody CreateJobRequest request,
                                       Authentication authentication) {
        return ResponseEntity.ok(jobService.createJob(request, authentication));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<Job>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer experienceYears,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                jobService.searchJobs(title, location, experienceYears, minSalary, maxSalary, pageable)
        );
    }
    @GetMapping("/employer")
    public List<Job> employerJobs(Authentication authentication) {
        return jobService.getEmployerJobs(authentication.getName());
    }

    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id,
                         @RequestBody Job job,
                         Authentication authentication) {
        return jobService.updateJob(authentication.getName(), id, job);
    }

    @PutMapping("/{id}/close")
    public Job closeJob(@PathVariable Long id,
                        Authentication authentication) {
        return jobService.closeJob(authentication.getName(), id);
    }

    @PutMapping("/{id}/fill")
    public Job markAsFilled(@PathVariable Long id,
                            Authentication authentication) {
        return jobService.markAsFilled(authentication.getName(), id);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id,
                          Authentication authentication) {
        jobService.deleteJob(authentication.getName(), id);
    }
}