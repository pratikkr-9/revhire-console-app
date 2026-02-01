package dao;

import model.Resume;
import util.DBUtil;

import exception.DatabaseException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResumeDAO {

    private static final Logger logger =
            LogManager.getLogger(ResumeDAO.class);

    public void saveOrUpdate(Resume r)
            throws DatabaseException {

        String sql =
            "REPLACE INTO resume(seeker_id,education,experience,skills,certifications) " +
            "VALUES(?,?,?,?,?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getSeekerId());
            ps.setString(2, r.getEducation());
            ps.setString(3, r.getExperience());
            ps.setString(4, r.getSkills());
            ps.setString(5, r.getCertifications());

            ps.executeUpdate();

            logger.info("Resume saved/updated for seekerId={}", r.getSeekerId());

        } catch (SQLException e) {
            logger.error("Error saving resume", e);
            throw new DatabaseException(
                "Unable to save resume.",
                e
            );
        }
    }

    public Resume getResume(int seekerId)
            throws DatabaseException {

        String sql = "SELECT * FROM resume WHERE seeker_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                logger.warn("Resume not found for seekerId={}", seekerId);
                return null; // Business case: resume not yet created
            }

            Resume r = new Resume();
            r.setSeekerId(seekerId);
            r.setEducation(rs.getString("education"));
            r.setExperience(rs.getString("experience"));
            r.setSkills(rs.getString("skills"));
            r.setCertifications(rs.getString("certifications"));

            logger.info("Resume fetched for seekerId={}", seekerId);
            return r;

        } catch (SQLException e) {
            logger.error("Error fetching resume", e);
            throw new DatabaseException(
                "Unable to fetch resume.",
                e
            );
        }
    }
}
