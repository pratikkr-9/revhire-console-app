package com.example.revhire.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saved_jobs",
       uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}