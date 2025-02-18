package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.example.database.exception.*;

public class DatabaseAccessor {
    private static final String URL = "jdbc:sqlite:ApplicationDatabase.db";

    public static void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC is missing. Please add it to the project's dependencies.", e);
        }

        configureEnvironment();
        createTables();
    }

    private static void configureEnvironment() {
        final String foreignKeyOnCommand = "PRAGMA foreign_keys = ON;";

        try (
                Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(foreignKeyOnCommand);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to configure the database environment.", e);
        }
    }

    private static void createTables() {
        final String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "account TEXT PRIMARY KEY,"
                + "hashed_password TEXT NOT NULL,"
                + "name TEXT NOT NULL,"
                + "sign_up_time_utc TEXT NOT NULL DEFAULT (datetime('now'))"
                + ");";

        try (
                Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(createUsersTable);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables.", e);
        }
    }

    public static void addUser(String account, String hashedPassword, String name) throws DuplicateAccountException {
        final String insertUserCommand =
                "INSERT INTO users(account, hashed_password, name) VALUES(?, ?, ?)";

        try (
                Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(insertUserCommand)
        ) {
            pstmt.setString(1, account);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new org.example.database.exception.DuplicateAccountException(account);
            } else {
                throw new RuntimeException("Failed to add a user to the database.", e);
            }
        }
    }

    public static String showAllUsers() {
        final String selectAllUsersCommand = "SELECT account, hashed_password, name, sign_up_time_utc FROM users";
        StringBuilder result = new StringBuilder();

        try (
                Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectAllUsersCommand)
        ) {
            while (rs.next()) {
                result.append("Account: ").append(rs.getString("account"))
                        .append(", Hashed Password: ").append(rs.getString("hashed_password"))
                        .append(", Name: ").append(rs.getString("name"))
                        .append(", Sign Up Time: ").append(rs.getString("sign_up_time_utc"))
                        .append("\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve users from the database.", e);
        }

        return result.toString();
    }
}
