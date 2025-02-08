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
            // Initialize database
            initDatabase();
            
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

    private static void initDatabase() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            // Execute schema.sql
            String schema = new String(
                App.class.getResourceAsStream("/schema.sql").readAllBytes()
            );
            stmt.execute(schema);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
