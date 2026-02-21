package com.example.revhire.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revhire.entity.Application;
import com.example.revhire.entity.Job;
import com.example.revhire.entity.User;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByApplicant(User user);

    List<Application> findByJob(Job job);

    Optional<Application> findByJobAndApplicant(Job job, User user);
}
