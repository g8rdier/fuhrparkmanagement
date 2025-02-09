package de.fuhrpark;

import de.fuhrpark.ui.FuhrparkUI;
import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;

public class App {
    public static void main(String[] args) {
        // Initialize UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better UI experience
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
                
                // Initialize dependencies
                DataStore dataStore = new DataStore();
                
                // Create the UI with DataStore
                FuhrparkUI ui = new FuhrparkUI(dataStore);
                ui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Fehler beim Starten der Anwendung: " + e.getMessage(),
                    "Fehler",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
