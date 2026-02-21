package com.example.revhire.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.example.revhire.enums.JobType;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 3000)
    private String description;

    private String skills;

    private Integer experienceYears;

    private String education;

    private String location;

    private Double salaryMin;

    private Double salaryMax;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private LocalDateTime postedDate;

    private LocalDateTime deadline;

    private Integer openings;

    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;
}
