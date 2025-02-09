package de.fuhrpark;

import de.fuhrpark.ui.FuhrparkUI;
import de.fuhrpark.manager.FuhrparkManagerImpl;
import de.fuhrpark.manager.FuhrparkManager;

public class App {
    public static void main(String[] args) {
        // Initialize UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better UI experience
                javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
                
                FuhrparkManager manager = new FuhrparkManagerImpl();
                FuhrparkUI ui = new FuhrparkUI(manager);
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
