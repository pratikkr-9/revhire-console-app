package com.example.revhire.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.entity.Job;
import com.example.revhire.service.JobService;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public Job createJob(@RequestBody Job job,
                         Authentication authentication) {
        return jobService.createJob(authentication.getName(), job);
    }

    @GetMapping("/search")
    public List<Job> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary
    ) {
        return jobService.searchJobs(title, location, experience, minSalary, maxSalary);
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