package com.example.revhire.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.revhire.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>,
        JpaSpecificationExecutor<Job> {
	long countByEmployerId(Long employerId);

	long countByEmployerIdAndIsActiveTrue(Long employerId);

	long countByEmployerIdAndIsFilledTrue(Long employerId);
	
}
