package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseManager {
    public static final String URL = "jdbc:sqlite:DietDiaryApplicationDatabase.db";

    /**
     * Configure database environment.
     * Called once when the class is instantiated.
     * Currently, it only enables foreign key constraints.
     * Further configurations can be added here.
     */
    private static void configureEnvironment() {
        try (
                Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to configure database environment.", e);
        }
    }

    /**
     * Creates tables in the database.
     * Called once when the class is instantiated.
     */
    private static void createTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "account TEXT PRIMARY KEY," +
                "bcrypt_hashed_password TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "signup_time TEXT DEFAULT (datetime('now'))" +
                ");";

        try (
                Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(createUsersTable);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables.", e);
        }
    }

    /**
     * Initializes the database.
     * This method should be called once before calling DatabaseAccessor methods.
     */
    public static void initializeDatabase() {
        configureEnvironment();
        createTables();
    }
}