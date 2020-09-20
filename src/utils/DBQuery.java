package utils;

import java.sql.*;

import static utils.DBConnection.conn;

public class DBQuery {
    private static String query;
    private static PreparedStatement statement;
    private static ResultSet result;

    public static void makeQuery(String queryStatement) {
        try {
            statement = conn.prepareStatement(queryStatement);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static PreparedStatement getQuery() {
        return statement;
    }
}
