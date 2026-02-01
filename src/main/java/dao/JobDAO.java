package dao;

import model.Job;
import util.DBUtil;

import exception.DatabaseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    private static final Logger logger =
            LogManager.getLogger(JobDAO.class);

    public void postJob(Job job) throws DatabaseException {

        String sql =
            "INSERT INTO jobs(employer_id,title,description,location,salary,job_type) VALUES(?,?,?,?,?,?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, job.getEmployerId());
            ps.setString(2, job.getTitle());
            ps.setString(3, job.getDescription());
            ps.setString(4, job.getLocation());
            ps.setDouble(5, job.getSalary());
            ps.setString(6, job.getJobType());

            ps.executeUpdate();

            logger.info("Job posted successfully for employerId={}", job.getEmployerId());

        } catch (SQLException e) {
            logger.error("Error while posting job", e);
            throw new DatabaseException(
                "Unable to post job at this time. Please try again later.",
                e
            );
        }
    }

    public List<Job> getAllJobs() throws DatabaseException {

        List<Job> list = new ArrayList<>();
        String sql = "SELECT * FROM jobs";

        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Job j = new Job();
                j.setJobId(rs.getInt("job_id"));
                j.setEmployerId(rs.getInt("employer_id"));
                j.setTitle(rs.getString("title"));
                j.setDescription(rs.getString("description"));
                j.setLocation(rs.getString("location"));
                j.setSalary(rs.getDouble("salary"));
                j.setJobType(rs.getString("job_type"));
                list.add(j);
            }

            logger.info("Fetched {} jobs from database", list.size());

        } catch (SQLException e) {
            logger.error("Error while fetching jobs", e);
            throw new DatabaseException(
                "Unable to fetch jobs at this time.",
                e
            );
        }

        return list;
    }
    
    public List<Job> getJobsByEmployer(int employerId)
            throws DatabaseException {

        List<Job> list = new ArrayList<>();

        String sql = "SELECT * FROM jobs WHERE employer_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Job j = new Job();
                j.setJobId(rs.getInt("job_id"));
                j.setEmployerId(rs.getInt("employer_id"));
                j.setTitle(rs.getString("title"));
                j.setDescription(rs.getString("description"));
                j.setLocation(rs.getString("location"));
                j.setSalary(rs.getDouble("salary"));
                j.setJobType(rs.getString("job_type"));
                list.add(j);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Unable to fetch your jobs.", e);
        }

        return list;
    }

}
