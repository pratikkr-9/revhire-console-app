package com.example.revhire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.revhire.entity.Application;
import com.example.revhire.entity.Job;
import com.example.revhire.entity.User;
import com.example.revhire.enums.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends
        JpaRepository<Application, Long>,
        JpaSpecificationExecutor<Application> {

    List<Application> findByApplicant(User user);

    List<Application> findByJob(Job job);

    Optional<Application> findByJobAndApplicant(Job job, User user);

    List<Application> findByJobId(Long jobId);

    List<Application> findByJobIdAndStatus(Long jobId, ApplicationStatus status);

    long countByJobEmployerId(Long employerId);

    long countByJobEmployerIdAndStatus(
            Long employerId,
            ApplicationStatus status
    );
}