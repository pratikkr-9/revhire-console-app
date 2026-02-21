package com.example.revhire.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.revhire.entity.Notification;
import com.example.revhire.entity.User;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}