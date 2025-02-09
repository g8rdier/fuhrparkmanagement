package de.fuhrpark;

import de.fuhrpark.persistence.repository.impl.FileDataStore;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.ui.FuhrparkUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Initialize components
            FileDataStore dataStore = new FileDataStore();
            FahrzeugService fahrzeugService = new FahrzeugServiceImpl(dataStore);
            FahrzeugFactory fahrzeugFactory = new FahrzeugFactoryImpl();

            // Load saved data
            dataStore.load();

            // Start UI in Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                FuhrparkUI ui = new FuhrparkUI(dataStore);
                ui.setVisible(true);
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
