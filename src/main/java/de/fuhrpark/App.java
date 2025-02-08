package de.fuhrpark;

import de.fuhrpark.ui.FuhrparkUI;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FuhrparkUI ui = new FuhrparkUI();
            ui.setVisible(true);
        });
    }
}
