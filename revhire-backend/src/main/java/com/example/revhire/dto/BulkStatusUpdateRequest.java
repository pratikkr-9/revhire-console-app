package com.example.revhire.dto;


import lombok.Data;

import java.util.List;

import com.example.revhire.enums.ApplicationStatus;

@Data
public class BulkStatusUpdateRequest {

    private List<Long> applicationIds;
    private ApplicationStatus status;
}