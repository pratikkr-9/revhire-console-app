package com.example.revhire.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployerDashboardResponse {

    private long totalJobs;
    private long activeJobs;
    private long filledJobs;
    private long totalApplications;
    private long pendingReviews;
}