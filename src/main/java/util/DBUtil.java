package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exception.DatabaseException;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/revhire";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Kumar@11";

    private DBUtil() {
    }

    public static Connection getConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException(
                "Failed to establish database connection. Please check DB server and credentials.",
                e
            );
        }
    }
}
