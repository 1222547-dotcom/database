package phase3;

import java.sql.Connection;

public class DBConfig {

    public static final String URL = "127.0.0.1";
    public static final String PORT = "3306";
    public static final String DB_NAME = "iCell_DB";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD1 = "0000";
    public static final String DB_PASSWORD2 = "246810";

    public static Connection getConnection() {
           try {
            DBConn db = new DBConn(URL, PORT, DB_NAME, DB_USERNAME, DB_PASSWORD1);
            Connection conn = db.connectDB();
            if (conn != null) {
                return conn;
            }
        } catch (Exception e) {
            System.out.println("First password attempt failed, trying fallback password...");
        }

        try {
            DBConn db = new DBConn(URL, PORT, DB_NAME, DB_USERNAME, DB_PASSWORD2);
            return db.connectDB();
        } catch (Exception e) {
            System.out.println("Database connection error on both passwords: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}