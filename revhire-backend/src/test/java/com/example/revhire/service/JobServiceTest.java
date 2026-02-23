package com.example.revhire.service;

import com.example.revhire.entity.Job;
import com.example.revhire.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void closeJob_ShouldSetIsActiveToFalse() {

        Job job = new Job();
        job.setId(1L);
        job.setIsActive(true);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.save(job)).thenReturn(job);

        Job closedJob = jobService.closeJob("test@company.com", 1L);

        assertFalse(closedJob.getIsActive());
        verify(jobRepository, times(1)).save(job);
    }
}