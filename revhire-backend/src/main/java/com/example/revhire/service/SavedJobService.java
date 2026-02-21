package com.example.revhire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.revhire.entity.Job;
import com.example.revhire.entity.SavedJob;
import com.example.revhire.entity.User;
import com.example.revhire.repository.JobRepository;
import com.example.revhire.repository.SavedJobRepository;
import com.example.revhire.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public SavedJob saveJob(String email, Long jobId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (savedJobRepository.findByJobAndUser(job, user).isPresent()) {
            throw new RuntimeException("Job already saved");
        }

        SavedJob savedJob = SavedJob.builder()
                .job(job)
                .user(user)
                .build();

        return savedJobRepository.save(savedJob);
    }

    public List<SavedJob> getSavedJobs(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedJobRepository.findByUser(user);
    }

    public void removeSavedJob(String email, Long jobId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        SavedJob savedJob = savedJobRepository.findByJobAndUser(job, user)
                .orElseThrow(() -> new RuntimeException("Saved job not found"));

        savedJobRepository.delete(savedJob);
    }
}