package service;

import dao.ApplicationDAO;
import model.Application;

import exception.DatabaseException;
import exception.ResourceNotFoundException;
import exception.ValidationException;

import java.util.List;

public class ApplicationService {

    private final ApplicationDAO dao = new ApplicationDAO();

    public void applyJob(int jobId, int seekerId)
            throws ValidationException, DatabaseException {

        if (jobId <= 0) {
            throw new ValidationException("Invalid job selected.");
        }
        if (seekerId <= 0) {
            throw new ValidationException("Invalid job seeker.");
        }

        dao.applyJob(jobId, seekerId);
    }

    public List<Application> getMyApplications(int seekerId)
            throws ValidationException, DatabaseException {

        if (seekerId <= 0) {
            throw new ValidationException("Invalid job seeker.");
        }

        return dao.getApplicationsBySeeker(seekerId);
    }

    public List<Application> getJobApplications(int jobId)
            throws ValidationException, DatabaseException, ResourceNotFoundException {

        if (jobId <= 0) {
            throw new ValidationException("Invalid job selected.");
        }

        return dao.getApplicationsByJob(jobId);
    }

    public void updateStatus(int appId, String status)
            throws ValidationException,
                   ResourceNotFoundException,
                   DatabaseException {
        if (appId <= 0) {
            throw new ValidationException("Invalid application ID.");
        }
        if (status == null || status.isBlank()) {
            throw new ValidationException("Status cannot be empty.");
        }

        dao.updateStatus(appId, status);
    }

    public void withdraw(int appId)
            throws ValidationException, DatabaseException {

        if (appId <= 0) {
            throw new ValidationException("Invalid application ID.");
        }

        dao.withdrawApplication(appId);
    }
}
