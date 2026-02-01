package dao;

import model.Notification;
import util.DBUtil;

import exception.DatabaseException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private static final Logger logger =
            LogManager.getLogger(NotificationDAO.class);

    public void addNotification(int userId, String message)
            throws DatabaseException {

        String sql =
            "INSERT INTO notifications(user_id,message) VALUES(?,?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

            logger.info("Notification added for userId={}", userId);

        } catch (SQLException e) {
            logger.error("Error adding notification", e);
            throw new DatabaseException(
                "Unable to send notification.",
                e
            );
        }
    }

    public List<Notification> getUserNotifications(int userId)
            throws DatabaseException {

        List<Notification> list = new ArrayList<>();
        String sql =
            "SELECT * FROM notifications WHERE user_id=? ORDER BY created_at DESC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setNotifId(rs.getInt("notif_id"));
                n.setUserId(userId);
                n.setMessage(rs.getString("message"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(n);
            }

            logger.info("Fetched {} notifications for userId={}",
                        list.size(), userId);

        } catch (SQLException e) {
            logger.error("Error fetching notifications", e);
            throw new DatabaseException(
                "Unable to fetch notifications.",
                e
            );
        }

        return list;
    }
}
