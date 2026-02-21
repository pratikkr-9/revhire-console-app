package com.example.revhire.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.revhire.entity.SavedJob;
import com.example.revhire.service.SavedJobService;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{jobId}")
    public SavedJob saveJob(@PathVariable Long jobId,
                            Authentication authentication) {
        return savedJobService.saveJob(authentication.getName(), jobId);
    }

    @GetMapping
    public List<SavedJob> getSavedJobs(Authentication authentication) {
        return savedJobService.getSavedJobs(authentication.getName());
    }

    @DeleteMapping("/{jobId}")
    public void removeSavedJob(@PathVariable Long jobId,
                               Authentication authentication) {
        savedJobService.removeSavedJob(authentication.getName(), jobId);
    }
}