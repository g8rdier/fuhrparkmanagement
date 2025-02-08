package de.fuhrpark.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    // Use in-memory database for testing
    private static final String URL = "jdbc:h2:./fuhrparkdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            // Explicitly load H2 driver
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Database connection established");
        return conn;
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            // Test if table exists, create if not
            String createTable = "CREATE TABLE IF NOT EXISTS fahrzeuge (" +
                "kennzeichen VARCHAR(20) PRIMARY KEY, " +
                "marke VARCHAR(50), " +
                "modell VARCHAR(50), " +
                "typ VARCHAR(20), " +
                "baujahr INT, " +
                "kilometerstand DOUBLE)";
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTable);
                System.out.println("Table 'fahrzeuge' verified/created");
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 