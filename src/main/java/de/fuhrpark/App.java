package de.fuhrpark;

import de.fuhrpark.ui.FuhrparkUI;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FuhrparkUI ui = new FuhrparkUI();  // Use default constructor
            ui.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            ui.pack();
            ui.setLocationRelativeTo(null);
            ui.setVisible(true);
        });
    }
}
