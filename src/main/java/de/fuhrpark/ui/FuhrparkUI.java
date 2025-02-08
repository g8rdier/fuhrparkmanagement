package de.fuhrpark.ui;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.ui.model.FahrzeugTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final FuhrparkManager manager;
    private final FahrzeugService fahrzeugService;
    private final FahrzeugFactory fahrzeugFactory;
    private final FahrzeugTableModel tableModel;
    
    // UI Components
    private final JTable fahrzeugTable;
    private final JComboBox<FahrzeugTyp> fahrzeugTypComboBox;
    private final JTextField kennzeichenField;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JTextField preisField;

    public FuhrparkUI(FuhrparkManager manager, FahrzeugService service, FahrzeugFactory factory) {
        this.manager = manager;
        this.fahrzeugService = service;
        this.fahrzeugFactory = factory;
        
        // Setup UI
        setTitle("Fuhrpark Verwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Initialize components
        tableModel = new FahrzeugTableModel();
        fahrzeugTable = new JTable(tableModel);
        fahrzeugTypComboBox = new JComboBox<>(FahrzeugTyp.values());
        kennzeichenField = new JTextField(10);
        markeField = new JTextField(10);
        modellField = new JTextField(10);
        preisField = new JTextField(10);

        // Layout
        setupLayout();
        
        // Load initial data
        refreshTable();
        
        pack();
        setLocationRelativeTo(null);
    }

    private void setupLayout() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        inputPanel.add(new JLabel("Typ:"));
        inputPanel.add(fahrzeugTypComboBox);
        inputPanel.add(new JLabel("Kennzeichen:"));
        inputPanel.add(kennzeichenField);
        inputPanel.add(new JLabel("Marke:"));
        inputPanel.add(markeField);
        inputPanel.add(new JLabel("Modell:"));
        inputPanel.add(modellField);
        inputPanel.add(new JLabel("Preis:"));
        inputPanel.add(preisField);

        JButton addButton = new JButton("Hinzufügen");
        addButton.addActionListener(e -> addFahrzeug());
        inputPanel.add(addButton);

        // Table Panel
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Bearbeiten");
        JButton deleteButton = new JButton("Löschen");
        JButton fahrtenbuchButton = new JButton("Fahrtenbuch");
        
        editButton.addActionListener(e -> editSelectedFahrzeug());
        deleteButton.addActionListener(e -> deleteSelectedFahrzeug());
        fahrtenbuchButton.addActionListener(e -> showFahrtenbuch());
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(fahrtenbuchButton);

        // Add to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        List<Fahrzeug> fahrzeuge = fahrzeugService.getAlleFahrzeuge();
        tableModel.setData(fahrzeuge);
    }

    private void addFahrzeug() {
        try {
            String typString = fahrzeugTypComboBox.getSelectedItem().toString();
            String kennzeichen = kennzeichenField.getText().trim();
            String marke = markeField.getText().trim();
            String modell = modellField.getText().trim();
            double preis = Double.parseDouble(preisField.getText().trim());

            manager.createFahrzeug(typString, kennzeichen, marke, modell, preis);
            refreshTable();
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie einen gültigen Preis ein.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Hinzufügen: " + e.getMessage());
        }
    }

    private void clearInputFields() {
        kennzeichenField.setText("");
        markeField.setText("");
        modellField.setText("");
        preisField.setText("");
        fahrzeugTypComboBox.setSelectedIndex(0);
    }

    private void editSelectedFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            Fahrzeug fahrzeug = tableModel.getRow(selectedRow);
            // TODO: Implement edit dialog
            JOptionPane.showMessageDialog(this, "Edit-Funktion noch nicht implementiert");
        }
    }

    private void deleteSelectedFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            Fahrzeug fahrzeug = tableModel.getRow(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Möchten Sie das Fahrzeug wirklich löschen?",
                "Fahrzeug löschen",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteFahrzeug(fahrzeug.getKennzeichen());
                refreshTable();
            }
        }
    }

    private void showFahrtenbuch() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            Fahrzeug fahrzeug = tableModel.getRow(selectedRow);
            // TODO: Implement Fahrtenbuch dialog
            JOptionPane.showMessageDialog(this, "Fahrtenbuch-Funktion noch nicht implementiert");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create components with proper error handling
                FahrzeugService service = new FahrzeugServiceImpl();
                FahrzeugFactory factory = new FahrzeugFactoryImpl();
                FuhrparkManager manager = new FuhrparkManager(service, factory);
                
                new FuhrparkUI(manager, service, factory).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Fehler beim Starten der Anwendung: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
} 
