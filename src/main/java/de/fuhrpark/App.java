package de.fuhrpark;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.DatabaseConfig;
import de.fuhrpark.persistence.impl.DatabaseDataStoreImpl;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.service.impl.ReparaturServiceImpl;
import de.fuhrpark.ui.FuhrparkUI;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            // Initialize database with detailed error handling
            if (!initDatabase()) {
                System.exit(1);
                return;
            }
            
            // Create data store and services
            DataStore dataStore = new DatabaseDataStoreImpl();
            FahrzeugService fahrzeugService = new FahrzeugServiceImpl(dataStore);
            FahrtenbuchService fahrtenbuchService = new FahrtenbuchServiceImpl(dataStore);
            ReparaturService reparaturService = new ReparaturServiceImpl(dataStore);

            // Launch UI
            SwingUtilities.invokeLater(() -> {
                var ui = new FuhrparkUI(fahrzeugService, fahrtenbuchService, reparaturService);
                ui.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Fehler beim Starten der Anwendung: " + e.getMessage(),
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean initDatabase() {
        try {
            // Test connection first
            if (!DatabaseConfig.testConnection()) {
                throw new RuntimeException("Database connection test failed");
            }
            System.out.println("Database connection test successful");

            // First try to establish connection
            try (var ignored = DatabaseConfig.getConnection()) {
                System.out.println("Database connection established successfully");
            } catch (Exception e) {
                throw new RuntimeException("Could not connect to database", e);
            }

            // Then try to read schema file
            InputStream schemaStream = App.class.getResourceAsStream("/schema.sql");
            if (schemaStream == null) {
                throw new RuntimeException("Could not find schema.sql in resources");
            }

            // Read schema content
            String schema = new String(schemaStream.readAllBytes());
            System.out.println("Schema file read successfully");

            // Execute schema
            try (Connection conn = DatabaseConfig.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute(schema);
                System.out.println("Schema executed successfully");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Fehler beim Initialisieren der Datenbank:\n" + e.getMessage(),
                "Datenbankfehler",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
