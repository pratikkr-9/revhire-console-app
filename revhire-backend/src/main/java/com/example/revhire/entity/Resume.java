package com.example.revhire.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String objective;

    @Column(length = 2000)
    private String education;

    @Column(length = 3000)
    private String experience;

    @Column(length = 2000)
    private String skills;

    @Column(length = 2000)
    private String projects;

    @Column(length = 2000)
    private String certifications;

    private String uploadedFilePath;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}