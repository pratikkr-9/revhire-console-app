package service;

import dao.ResumeDAO;
import model.Resume;

import exception.DatabaseException;
import exception.ValidationException;

public class ResumeService {

    private final ResumeDAO dao = new ResumeDAO();

    public void saveOrUpdate(Resume resume)
            throws ValidationException, DatabaseException {

        if (resume == null) {
            throw new ValidationException("Resume cannot be null.");
        }
        if (resume.getSeekerId() <= 0) {
            throw new ValidationException("Invalid job seeker.");
        }

        dao.saveOrUpdate(resume);
    }

    public Resume getResume(int seekerId)
            throws ValidationException, DatabaseException {

        if (seekerId <= 0) {
            throw new ValidationException("Invalid job seeker.");
        }

        // null = resume not created yet (valid business case)
        return dao.getResume(seekerId);
    }
}
