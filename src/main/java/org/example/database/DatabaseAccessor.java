package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.example.database.exception.*;

/**
 * This class provides methods to initialize and access the database.
 * Only basic CRUD operations for data entities are implemented here.
 */
public class DatabaseAccessor {
    private static final String URL = "jdbc:sqlite:ApplicationDatabase.db";

    /**
     * Must be called once before any other method in this class.
     * This method will configure the environment, create the database file and tables if they do not exist.
     */
    public static void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC is missing. Please add it to the project's dependencies.", e);
        }

        configureEnvironment();
        createTables();
    }

    /**
     * Call the method once to configure the database environment.
     * So far, it only enables foreign key constraints.
     * If further configurations are needed, they can be added here.
     * This method is called in the initialize method.
     */
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

    /**
     * Call the method once to create the necessary tables for the application.
     * If the tables already exist, this method will not recreate them or modify existing contents.
     * This method is called in the initialize method.
     */
    private static void createTables() {
        final String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "account TEXT PRIMARY KEY,"
                + "hashed_password TEXT NOT NULL,"
                + "name TEXT NOT NULL,"
                + "local_sign_up_time TEXT NOT NULL"
                + ");";

        final String createFoodsTable = "CREATE TABLE IF NOT EXISTS foods ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "serving_type TEXT NOT NULL,"
                + "serving_size REAL NOT NULL,"
                + "serving_unit TEXT NOT NULL,"
                + "creator_account TEXT NOT NULL,"
                + "local_creation_time TEXT NOT NULL,"
                + "is_public INTEGER NOT NULL CHECK(is_public IN (0, 1)),"
                + "FOREIGN KEY (creator_account) REFERENCES users(account)"
                + ");";

        final String createNutritionFactsTable = "CREATE TABLE IF NOT EXISTS nutrition_facts ("
                + "food_id INTEGER,"
                + "nutrient TEXT,"
                + "average_quantity_per_serving REAL,"
                + "quantity_unit TEXT,"
                + "PRIMARY KEY (food_id, nutrient),"
                + "FOREIGN KEY (food_id) REFERENCES foods(id)"
                + ");";

        final String createLogsTable = "CREATE TABLE IF NOT EXISTS logs ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "creator_account TEXT NOT NULL,"
                + "title TEXT,"
                + "local_creation_time TEXT NOT NULL,"
                + "food_id INTEGER NOT NULL,"
                + "quantity REAL NOT NULL,"
                + "FOREIGN KEY (creator_account) REFERENCES users(account),"
                + "FOREIGN KEY (food_id) REFERENCES foods(id)"
                + ");";

        try (
                Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(createUsersTable);
            stmt.execute(createFoodsTable);
            stmt.execute(createNutritionFactsTable);
            stmt.execute(createLogsTable);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables.", e);
        }
    }

    public static void insertUser(
            String account, String hashedPassword, String name) throws DuplicateAccountException {
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
        final String selectAllUsersCommand = "SELECT account, hashed_password, name, utc_sign_up_time FROM users";
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
                        .append(", Sign Up Time: ").append(rs.getString("utc_sign_up_time"))
                        .append("\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve users from the database.", e);
        }

        return result.toString();
    }
}
