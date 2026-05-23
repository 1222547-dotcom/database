package phase3;

import java.sql.Connection;

public class DBConfig {
    
    public static final String URL = "127.0.0.1";
    public static final String PORT = "3306";
    public static final String DB_NAME = "iCell_DB";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "0000";
    
    public static Connection getConnection() {
        try {
            DBConn db = new DBConn(URL, PORT, DB_NAME, DB_USERNAME, DB_PASSWORD);
            return db.connectDB();
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}