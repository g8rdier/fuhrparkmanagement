package de.fuhrpark;

import de.fuhrpark.persistence.DataStore;
import de.fuhrpark.persistence.impl.DataStoreImpl;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.ui.FuhrparkUI;

public class App {
    public static void main(String[] args) {
        // Initialize persistence layer (BANF4)
        DataStore dataStore = new DataStoreImpl();
        
        // Initialize services (BANF3 - dependency injection)
        FahrzeugService fahrzeugService = new FahrzeugServiceImpl(dataStore);
        FahrtenbuchService fahrtenbuchService = new FahrtenbuchServiceImpl(dataStore);
        
        // Initialize UI with all required services
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better UI experience
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
                
                FuhrparkUI ui = new FuhrparkUI();
                ui.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                ui.setTitle("Fuhrpark Verwaltung");
                ui.pack();
                ui.setLocationRelativeTo(null);
                ui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
