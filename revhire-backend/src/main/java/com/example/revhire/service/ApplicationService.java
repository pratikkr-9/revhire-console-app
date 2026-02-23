package com.example.revhire.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.revhire.dto.ApplyRequest;
import com.example.revhire.entity.Application;
import com.example.revhire.entity.Job;
import com.example.revhire.entity.User;
import com.example.revhire.enums.ApplicationStatus;
import com.example.revhire.enums.Role;
import com.example.revhire.repository.ApplicationRepository;
import com.example.revhire.repository.JobRepository;
import com.example.revhire.repository.UserRepository;
import com.example.revhire.specification.ApplicationSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final UserRepository userRepository;
	private final JobRepository jobRepository;
	private final NotificationService notificationService;

	public Application applyForJob(Long jobId, ApplyRequest request, Authentication authentication) {

		User applicant = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (applicant.getRole() != Role.JOB_SEEKER) {
			throw new RuntimeException("Only job seekers can apply");
		}

		Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

		if (applicationRepository.findByJobAndApplicant(job, applicant).isPresent()) {
			throw new RuntimeException("You have already applied for this job");
		}

		Application application = new Application();
		application.setJob(job);
		application.setApplicant(applicant);
		application.setStatus(ApplicationStatus.APPLIED);
		application.setCoverLetter(request.getCoverLetter());
		application.setAppliedDate(LocalDateTime.now());

		Application savedApplication = applicationRepository.save(application);

		notificationService.createNotification(job.getEmployer(),
				"New application received for your job: " + job.getTitle());

		return savedApplication;
	}

	public void withdrawApplication(Long applicationId, String reason, Authentication authentication) {

		User applicant = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Application application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Application not found"));

		if (!application.getApplicant().getId().equals(applicant.getId())) {
			throw new RuntimeException("Unauthorized action");
		}

		application.setStatus(ApplicationStatus.WITHDRAWN);
		application.setWithdrawReason(reason);

		applicationRepository.save(application);
	}

	public List<Application> viewMyApplications(String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		return applicationRepository.findByApplicant(user);
	}

	public List<Application> getApplicantsForJob(String email, Long jobId, ApplicationStatus status) {

		Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

		if (!job.getEmployer().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized action");
		}

		if (status != null) {
			return applicationRepository.findByJobIdAndStatus(jobId, status);
		}

		return applicationRepository.findByJobId(jobId);
	}

	public Application updateApplicationStatus(String email, Long applicationId, ApplicationStatus status,
			String note) {

		Application application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Application not found"));

		if (!application.getJob().getEmployer().getEmail().equals(email)) {
			throw new RuntimeException("Unauthorized action");
		}

		application.setStatus(status);
		application.setEmployerNote(note);

		Application updated = applicationRepository.save(application);

		notificationService.createNotification(application.getApplicant(),
				"Your application for job '" + application.getJob().getTitle() + "' has been updated to: " + status);

		return updated;
	}

	public List<Application> bulkUpdateStatus(String email, List<Long> applicationIds, ApplicationStatus status) {

		List<Application> applications = applicationRepository.findAllById(applicationIds);

		for (Application application : applications) {

			if (!application.getJob().getEmployer().getEmail().equals(email)) {
				throw new RuntimeException("Unauthorized action");
			}

			application.setStatus(status);
		}

		return applicationRepository.saveAll(applications);
	}

	public Page<Application> searchApplicants(Long jobId, ApplicationStatus status, LocalDateTime appliedAfter,
			Integer experienceYears, String skills, String education, Pageable pageable) {

		Specification<Application> spec = ApplicationSpecification.filterApplicants(jobId, status, appliedAfter,
				experienceYears, skills, education);

		return applicationRepository.findAll(spec, pageable);
	}

	public void updateApplicationStatusBulk(List<Long> applicationIds, ApplicationStatus newStatus, String employerNote,
			Authentication authentication) {

		User employer = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("Employer not found"));

		if (employer.getRole() != Role.EMPLOYER) {
			throw new RuntimeException("Only employers can update applications");
		}

		List<Application> applications = applicationRepository.findAllById(applicationIds);

		for (Application application : applications) {

			// Ensure employer owns the job
			if (!application.getJob().getEmployer().getId().equals(employer.getId())) {
				throw new RuntimeException("Unauthorized access to application");
			}

			application.setStatus(newStatus);
			
			notificationService.createNotification(
			        application.getApplicant(),
			        "Your application for job '" +
			        application.getJob().getTitle() +
			        "' has been updated to: " + newStatus
			);

			if (employerNote != null && !employerNote.isEmpty()) {
				application.setEmployerNote(employerNote);
			}
		}

		applicationRepository.saveAll(applications);
	}
	
	
	public Application updateApplicationStatus(Long applicationId, ApplicationStatus status, String note,
			Authentication authentication) {

		User employer = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (employer.getRole() != Role.EMPLOYER) {
			throw new RuntimeException("Only employers can update status");
		}

		Application application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Application not found"));

// Ensure employer owns this job
		if (!application.getJob().getEmployer().getId().equals(employer.getId())) {
			throw new RuntimeException("Unauthorized action");
		}

		application.setStatus(status);

		if (note != null && !note.isEmpty()) {
			application.setEmployerNote(note);
		}

		Application updated = applicationRepository.save(application);

//  Notify Job Seeker
		notificationService.createNotification(application.getApplicant(),
				"Your application for '" + application.getJob().getTitle() + "' has been updated to: " + status);

		return updated;
	}
}
