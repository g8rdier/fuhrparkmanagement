package de.fuhrpark;

import de.fuhrpark.ui.FuhrparkUI;
import de.fuhrpark.manager.FuhrparkManager;

public class App {
    public static void main(String[] args) {
        // Initialize UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better UI experience
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
                
                FuhrparkManager manager = new FuhrparkManager();
                FuhrparkUI ui = new FuhrparkUI(manager);
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
