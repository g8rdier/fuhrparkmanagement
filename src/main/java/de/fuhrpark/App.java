package de.fuhrpark;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.ui.FuhrparkUI;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FahrzeugFactoryImpl factory = new FahrzeugFactoryImpl();
                FuhrparkUI ui = new FuhrparkUI(factory);
                ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ui.pack();
                ui.setLocationRelativeTo(null);
                ui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
