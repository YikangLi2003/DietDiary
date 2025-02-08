package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection = null;
    private static final String URL = "jdbc:sqlite:database.db";

    private DatabaseManager() {
        // private constructor to prevent instantiation
    }

    public static void connectToDatabase() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                connection.createStatement().execute("PRAGMA foreign_keys = ON;");
                System.out.println("Connection to SQLite has been established.");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to the database.", e);
            }
        }
    }

    public static void disconnectFromDatabase() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection to SQLite has been closed.");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close the connection to the database.", e);
            }
        }
    }

    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (\n"
                + "    id INTEGER PRIMARY KEY,\n"
                + "    name TEXT NOT NULL,\n"
                + "    status TEXT NOT NULL\n"
                + ");";
        try {
            connection.createStatement().execute(sql);
            System.out.println("Tables has been created.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables.", e);
        }
    }
}
