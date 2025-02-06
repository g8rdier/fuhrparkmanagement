package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;

public class FuhrparkUI extends JFrame {
    private final FahrzeugService fahrzeugService;
    private final FahrtenbuchService fahrtenbuchService;
    private final ReparaturService reparaturService;
    private JTable fahrzeugTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton fahrtenbuchButton;
    private JButton reparaturButton;
    private FahrzeugTableModel tableModel;

    public FuhrparkUI(FahrzeugService fahrzeugService, 
                     FahrtenbuchService fahrtenbuchService,
                     ReparaturService reparaturService) {
        this.fahrzeugService = fahrzeugService;
        this.fahrtenbuchService = fahrtenbuchService;
        this.reparaturService = reparaturService;
        initializeUI();
        updateFahrzeugTable();
    }

    private void updateFahrzeugTable() {
        tableModel.updateData(fahrzeugService.getAlleFahrzeuge());
    }

    private void initializeUI() {
        setTitle("Fuhrparkverwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create table
        String[] columns = {"Kennzeichen", "Marke", "Modell", "Typ", "Baujahr", "Status", "Wert"};
        fahrzeugTable = new JTable(new FahrzeugTableModel(columns));
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Fahrzeug hinzufügen");
        editButton = new JButton("Bearbeiten");
        deleteButton = new JButton("Löschen");
        fahrtenbuchButton = new JButton("Fahrtenbuch");
        reparaturButton = new JButton("Reparaturen");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(fahrtenbuchButton);
        buttonPanel.add(reparaturButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> showAddFahrzeugDialog());
        editButton.addActionListener(e -> editSelectedFahrzeug());
        deleteButton.addActionListener(e -> deleteSelectedFahrzeug());
        fahrtenbuchButton.addActionListener(e -> showFahrtenbuch());
        reparaturButton.addActionListener(e -> showReparaturen());

        add(mainPanel);
    }

    private void showAddFahrzeugDialog() {
        // TODO: Implement add dialog
        JOptionPane.showMessageDialog(this, "Fahrzeug hinzufügen Dialog wird implementiert");
    }

    private void editSelectedFahrzeug() {
        // TODO: Implement edit functionality
        JOptionPane.showMessageDialog(this, "Bearbeiten wird implementiert");
    }

    private void deleteSelectedFahrzeug() {
        // TODO: Implement delete functionality
        JOptionPane.showMessageDialog(this, "Löschen wird implementiert");
    }

    private void showFahrtenbuch() {
        // TODO: Implement logbook view
        JOptionPane.showMessageDialog(this, "Fahrtenbuch wird implementiert");
    }

    private void showReparaturen() {
        // TODO: Implement repair view
        JOptionPane.showMessageDialog(this, "Reparaturen wird implementiert");
    }
} 