package de.fuhrpark.ui;

import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final FahrzeugService fahrzeugService;
    private final FahrtenbuchService fahrtenbuchService;
    private final ReparaturService reparaturService;
    private final JTable fahrzeugTable;
    private final FahrzeugTableModel tableModel;

    public FuhrparkUI(FahrzeugService fahrzeugService, 
                     FahrtenbuchService fahrtenbuchService,
                     ReparaturService reparaturService) {
        this.fahrzeugService = fahrzeugService;
        this.fahrtenbuchService = fahrtenbuchService;
        this.reparaturService = reparaturService;
        
        setTitle("Fuhrpark Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        // Initialize table
        tableModel = new FahrzeugTableModel();
        fahrzeugTable = new JTable(tableModel);
        add(new JScrollPane(fahrzeugTable), BorderLayout.CENTER);
        
        // Add toolbar with buttons
        JToolBar toolbar = new JToolBar();
        toolbar.add(createButton("Hinzufügen", this::showAddDialog));
        toolbar.add(createButton("Bearbeiten", this::showEditDialog));
        toolbar.add(createButton("Löschen", this::deleteSelectedFahrzeug));
        toolbar.add(createButton("Fahrtenbuch", this::showLogbookView));
        toolbar.add(createButton("Reparaturen", this::showRepairView));
        add(toolbar, BorderLayout.NORTH);
        
        refreshTable();
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void refreshTable() {
        List<Fahrzeug> fahrzeuge = fahrzeugService.getAlleFahrzeuge();
        tableModel.setFahrzeuge(fahrzeuge);
    }

    private void showAddDialog() {
        // Will implement dialog functionality
        JOptionPane.showMessageDialog(this, "Add functionality coming soon!");
    }

    private void showEditDialog() {
        // Will implement edit functionality
        JOptionPane.showMessageDialog(this, "Edit functionality coming soon!");
    }

    private void deleteSelectedFahrzeug() {
        // Will implement delete functionality
        JOptionPane.showMessageDialog(this, "Delete functionality coming soon!");
    }

    private void showLogbookView() {
        // Will implement logbook view
        JOptionPane.showMessageDialog(this, "Logbook view coming soon!");
    }

    private void showRepairView() {
        // Will implement repair view
        JOptionPane.showMessageDialog(this, "Repair view coming soon!");
    }
} 