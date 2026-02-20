package com.example.revhire.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revhire.entity.Resume;
import com.example.revhire.entity.User;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByUser(User user);
}
