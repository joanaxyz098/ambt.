package com.example.demo;

import java.sql.*;

public class Database {
    public static final String URL = "jdbc:mysql://localhost:3306/csit228f1";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public ResultSet executeQueryWithResultSet(String query, Object... params) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query);

            // Set parameters if any
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            return statement.executeQuery(); // Return ResultSet
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving data: " + e.getMessage(), e);
        }
    }

    public void executeQuery(String query, Object... values) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            if(statement.executeUpdate() > 0) {
                System.out.println("SUCCESSFUL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
