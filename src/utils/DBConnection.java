package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U07wmh";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver and Connection Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    static Connection conn;

    private static final String username = "U07wmh";
    private static final String password = "53689153753";

    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successful.");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
