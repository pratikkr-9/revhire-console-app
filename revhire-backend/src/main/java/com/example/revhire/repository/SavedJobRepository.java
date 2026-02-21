package com.example.revhire.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revhire.entity.Job;
import com.example.revhire.entity.SavedJob;
import com.example.revhire.entity.User;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByUser(User user);

    Optional<SavedJob> findByJobAndUser(Job job, User user);
}