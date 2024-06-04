package org.example.system;

import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseIntegration {
    private static final String DATABASE_URL = "jdbc:mariadb://localhost:3306/githubAPI";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "root";

    private Connection connection;

    public void connectToDatabase() throws Exception {
        try {
            connection = (Connection) DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        } catch (Exception e) {
            throw new Exception("Failed to connect to database: " + e.getMessage());
        }
        System.out.println("Connected to database.");
    }

    public void closeConnection() throws Exception {
        try {
            connection.close();
        } catch (Exception e) {
            throw new Exception("Failed to close connection: " + e.getMessage());
        }
        System.out.println("Connection closed.");
    }

    public void addUser(String login, String name, String createdAt) throws Exception {
        String newCreatedAt = createdAt.substring(0, 10); // 2021-10-01T00:00:00Z -> 2021-10-01

        String query = "INSERT INTO users (login, name, created_at) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, login);
        statement.setString(2, name);
        statement.setString(3, newCreatedAt);
        statement.executeUpdate();
    }
}
