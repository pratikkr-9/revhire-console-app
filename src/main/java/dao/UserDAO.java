package dao;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import model.User;
import util.DBUtil;
import exception.AuthenticationException;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static final Logger logger =
            LogManager.getLogger(UserDAO.class);

    public void register(User user) throws DatabaseException {
        String sql =
            "INSERT INTO users(role,name,email,password) VALUES(?,?,?,?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getRole());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());

            ps.executeUpdate();

            logger.info("User registered successfully: {}", user.getEmail());

        } catch (SQLException e) {
            logger.error("Database error while registering user", e);
            throw new DatabaseException(
                "Unable to register user at this time. Please try again later.",
                e
            );
        }
    }

    public User login(String email, String password)
            throws AuthenticationException, DatabaseException {

        String sql =
            "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                logger.warn("Invalid login attempt for email: {}", email);
                throw new AuthenticationException(
                    "Invalid email or password."
                );
            }

            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setRole(rs.getString("role"));
            u.setName(rs.getString("name"));

            logger.info("User logged in successfully: {}", email);
            return u;

        } catch (SQLException e) {
            logger.error("Database error during login", e);
            throw new DatabaseException(
                "Login service is currently unavailable. Please try again later.",
                e
            );
        }
    }
}
