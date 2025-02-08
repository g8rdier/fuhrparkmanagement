package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.FahrtenbuchEintrag;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;

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
