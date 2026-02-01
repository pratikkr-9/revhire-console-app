package service;

import dao.JobDAO;
import model.Job;

import exception.DatabaseException;
import exception.ValidationException;

import java.util.List;

public class JobService {

    private final JobDAO dao = new JobDAO();

    public void postJob(Job job)
            throws ValidationException, DatabaseException {

        if (job == null) {
            throw new ValidationException("Job details cannot be null.");
        }
        if (job.getEmployerId() <= 0) {
            throw new ValidationException("Invalid employer.");
        }
        if (job.getTitle() == null || job.getTitle().isBlank()) {
            throw new ValidationException("Job title is required.");
        }
        if (job.getLocation() == null || job.getLocation().isBlank()) {
            throw new ValidationException("Job location is required.");
        }

        dao.postJob(job);
    }

    public List<Job> getAllJobs()
            throws DatabaseException {

        return dao.getAllJobs();
    }
    
    public List<Job> getMyJobs(int employerId)
            throws ValidationException, DatabaseException {

        if (employerId <= 0) {
            throw new ValidationException("Invalid employer.");
        }

        return dao.getJobsByEmployer(employerId);
    }

}
