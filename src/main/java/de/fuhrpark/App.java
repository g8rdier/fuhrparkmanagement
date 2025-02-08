package de.fuhrpark;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.DatabaseConfig;
import de.fuhrpark.persistence.impl.DatabaseDataStoreImpl;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.ReparaturServiceImpl;
import de.fuhrpark.ui.FuhrparkUI;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
            // First ensure H2 driver is loaded
            Class.forName("org.h2.Driver");
            System.out.println("H2 Driver loaded successfully");

            // Initialize database with detailed error handling
            if (!initDatabase()) {
                System.err.println("Failed to initialize database");
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
                FuhrparkUI ui = new FuhrparkUI(fahrzeugService, fahrtenbuchService, reparaturService);
                ui.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Fehler beim Starten der Anwendung: " + e.getMessage(),
                "Fehler",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static boolean initDatabase() {
        try {
            // Test connection and create tables if needed
            if (!DatabaseConfig.testConnection()) {
                throw new RuntimeException("Database connection test failed");
            }
            System.out.println("Database connection test successful");

            // Initialize schema
            try (Connection conn = DatabaseConfig.getConnection()) {
                // Create tables if they don't exist
                String schema = """
                    CREATE TABLE IF NOT EXISTS fahrzeuge (
                        kennzeichen VARCHAR(20) PRIMARY KEY,
                        marke VARCHAR(50),
                        modell VARCHAR(50),
                        typ VARCHAR(20),
                        baujahr INT,
                        kilometerstand DOUBLE
                    );
                    
                    CREATE TABLE IF NOT EXISTS reparaturen (
                        id IDENTITY PRIMARY KEY,
                        fahrzeug_kennzeichen VARCHAR(20),
                        datum DATE,
                        beschreibung VARCHAR(500),
                        kosten DOUBLE,
                        werkstatt VARCHAR(100),
                        FOREIGN KEY (fahrzeug_kennzeichen) REFERENCES fahrzeuge(kennzeichen)
                    );
                    
                    CREATE TABLE IF NOT EXISTS fahrtenbuch (
                        id IDENTITY PRIMARY KEY,
                        fahrzeug_kennzeichen VARCHAR(20),
                        datum DATE,
                        start_km DOUBLE,
                        end_km DOUBLE,
                        zweck VARCHAR(200),
                        FOREIGN KEY (fahrzeug_kennzeichen) REFERENCES fahrzeuge(kennzeichen)
                    );
                """;
                
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(schema);
                    System.out.println("Database schema initialized successfully");
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
