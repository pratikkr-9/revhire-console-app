package com.example.revhire.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revhire.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}