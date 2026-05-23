package phase3;

import phase3.DBConfig;
import phase3.User;
import java.sql.*;

public class UserDAO {

    public User login(String username, String password) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBConfig.getConnection();

            if (con == null) {
                System.out.println("Login error: Could not establish a database connection with either password.");
                return null;
            }

            System.out.println("Connection established successfully!");

            String SQL = "SELECT * FROM User WHERE Username = ? AND Password = ?";
            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("User_ID"));
                user.setUsername(rs.getString("Username"));
                user.setFirstName(rs.getString("First_Name"));
                user.setLastName(rs.getString("Last_Name"));
                user.setEmail(rs.getString("Email"));
                user.setUserType(rs.getString("User_Type"));
                user.setAccessLevel(rs.getInt("Access_Level"));

                System.out.println("Login successful for user: " + username);
                return user;
            }

        } catch (SQLException e) {
            System.out.println("Login database query error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) {
                    con.close();
                    System.out.println("Connection safely closed");
                }
            } catch (SQLException ex) {
                System.out.println("Error while closing database resources: " + ex.getMessage());
            }
        }

        return null;
    }
}