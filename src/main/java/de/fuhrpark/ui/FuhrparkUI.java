package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import javax.swing.*;
import java.awt.*;

public class FuhrparkUI extends JFrame {
    private final JList<Fahrzeug> fahrzeugList;
    private final DefaultListModel<Fahrzeug> listModel;
    
    public FuhrparkUI() {
        super("Fuhrpark Verwaltung");
        
        listModel = new DefaultListModel<>();
        fahrzeugList = new JList<>(listModel);
        
        setLayout(new BorderLayout());
        add(new JScrollPane(fahrzeugList), BorderLayout.CENTER);
        
        // Add sample data
        listModel.addElement(new PKW("B-AA 123"));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FuhrparkUI().setVisible(true);
        });
    }
} 
