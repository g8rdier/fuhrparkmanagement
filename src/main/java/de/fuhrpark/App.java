package de.fuhrpark;

import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.ui.FuhrparkUI;
import de.fuhrpark.persistence.FileDataStore;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FileDataStore dataStore = new FileDataStore("fahrzeuge.json");
                FahrzeugFactoryImpl factory = new FahrzeugFactoryImpl();
                FuhrparkUI ui = new FuhrparkUI(factory);
                ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ui.pack();
                ui.setLocationRelativeTo(null);
                ui.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Fehler beim Starten der Anwendung: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
