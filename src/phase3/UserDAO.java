package phase3;

import phase3.DBConn;
import phase3.DBConfig;
import phase3.User;
import java.sql.*;

public class UserDAO {
    
    public User login(String username, String password) {
        Connection con = null;
        
        try {
            // الاتصال بطريقة الدكتور
            DBConn a = new DBConn(DBConfig.URL, DBConfig.PORT, DBConfig.DB_NAME, 
                                   DBConfig.DB_USERNAME, DBConfig.DB_PASSWORD);
            con = a.connectDB();
            System.out.println("Connection established");
            
            String SQL = "SELECT * FROM User WHERE Username = ? AND Password = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("User_ID"));
                user.setUsername(rs.getString("Username"));
                user.setFirstName(rs.getString("First_Name"));
                user.setLastName(rs.getString("Last_Name"));
                user.setEmail(rs.getString("Email"));
                user.setUserType(rs.getString("User_Type"));
                user.setAccessLevel(rs.getInt("Access_Level"));
                
                rs.close();
                pstmt.close();
                con.close();
                System.out.println("Connection closed");
                return user;
            }
            
            rs.close();
            pstmt.close();
            con.close();
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}