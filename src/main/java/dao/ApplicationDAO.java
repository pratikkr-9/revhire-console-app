package dao;

import model.Application;
import util.DBUtil;

import exception.DatabaseException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    private static final Logger logger =
            LogManager.getLogger(ApplicationDAO.class);

    public void applyJob(int jobId, int seekerId) throws DatabaseException {

        String sql =
            "INSERT INTO applications(job_id,seeker_id,status) VALUES(?,?,?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, seekerId);
            ps.setString(3, "Applied");

            ps.executeUpdate();

            logger.info("Job applied | JobID={}, SeekerID={}", jobId, seekerId);

        } catch (SQLException e) {
            logger.error("Error applying for job", e);
            throw new DatabaseException(
                "Unable to apply for the job at this time.",
                e
            );
        }
    }

    public List<Application> getApplicationsBySeeker(int seekerId)
            throws DatabaseException {

        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE seeker_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application a = new Application();
                a.setAppId(rs.getInt("app_id"));
                a.setJobId(rs.getInt("job_id"));
                a.setStatus(rs.getString("status"));
                a.setApplyDate(rs.getTimestamp("apply_date"));
                list.add(a);
            }

            logger.info("Fetched {} applications for seekerId={}",
                        list.size(), seekerId);

        } catch (SQLException e) {
            logger.error("Error fetching applications by seeker", e);
            throw new DatabaseException(
                "Unable to fetch applications.",
                e
            );
        }

        return list;
    }

    public List<Application> getApplicationsByJob(int jobId)
            throws DatabaseException {

        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE job_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application a = new Application();
                a.setAppId(rs.getInt("app_id"));
                a.setSeekerId(rs.getInt("seeker_id"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }

            logger.info("Fetched {} applicants for JobID={}",
                        list.size(), jobId);

        } catch (SQLException e) {
            logger.error("Error fetching applications by job", e);
            throw new DatabaseException(
                "Unable to fetch job applications.",
                e
            );
        }

        return list;
    }

    public void updateStatus(int appId, String status)
            throws DatabaseException {

        String fetchSql =
            "SELECT seeker_id, job_id FROM applications WHERE app_id=?";
        String updateSql =
            "UPDATE applications SET status=? WHERE app_id=?";

        try (Connection con = DBUtil.getConnection()) {

            int seekerId;
            int jobId;

            try (PreparedStatement ps1 = con.prepareStatement(fetchSql)) {
                ps1.setInt(1, appId);
                ResultSet rs = ps1.executeQuery();

                if (!rs.next()) {
                    throw new DatabaseException(
                        "Application not found for ID: " + appId
                    );
                }

                seekerId = rs.getInt("seeker_id");
                jobId = rs.getInt("job_id");
            }

            try (PreparedStatement ps2 = con.prepareStatement(updateSql)) {
                ps2.setString(1, status);
                ps2.setInt(2, appId);
                ps2.executeUpdate();
            }

            new NotificationDAO().addNotification(
                seekerId,
                "Your application status for Job ID " + jobId +
                " updated to: " + status
            );

            logger.info("ApplicationID={} (JobID={}) updated to {}",
                        appId, jobId, status);

        } catch (SQLException e) {
            logger.error("Error updating application status", e);
            throw new DatabaseException(
                "Unable to update application status.",
                e
            );
        }
    }

    public void withdrawApplication(int appId)
            throws DatabaseException {

        updateStatus(appId, "Withdrawn");
        logger.info("Application withdrawn | AppID={}", appId);
    }
}
