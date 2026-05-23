package phase3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/icell_db";
    private static final String USER = "root";

    private static final String[] TEST_PASSWORDS = {"0000", "246810"};

    private static String cachedPassword = null;

    public static Connection getConnection() throws SQLException {
        if (cachedPassword != null) {
            return DriverManager.getConnection(URL, USER, cachedPassword);
        }

        for (String password : TEST_PASSWORDS) {
            try {
                Connection conn = DriverManager.getConnection(URL, USER, password);
                if (conn != null) {
                    cachedPassword = password;
                    return conn;
                }
            } catch (SQLException e) {
            }
        }

        throw new SQLException("Database connection failed: Unable to verify credentials.");
    }
}