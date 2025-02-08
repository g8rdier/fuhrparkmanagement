package de.fuhrpark.ui;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.ui.model.FahrzeugTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
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
            
            JDialog dialog = new JDialog(this, "Fahrzeug bearbeiten", true);
            dialog.setLayout(new GridLayout(5, 2, 5, 5));
            
            JTextField markeField = new JTextField(fahrzeug.getMarke());
            JTextField modellField = new JTextField(fahrzeug.getModell());
            JTextField preisField = new JTextField(String.valueOf(fahrzeug.getPreis()));
            
            dialog.add(new JLabel("Kennzeichen:"));
            dialog.add(new JLabel(fahrzeug.getKennzeichen()));
            dialog.add(new JLabel("Marke:"));
            dialog.add(markeField);
            dialog.add(new JLabel("Modell:"));
            dialog.add(modellField);
            dialog.add(new JLabel("Preis:"));
            dialog.add(preisField);
            
            JButton saveButton = new JButton("Speichern");
            saveButton.addActionListener(e -> {
                try {
                    fahrzeug.setMarke(markeField.getText());
                    fahrzeug.setModell(modellField.getText());
                    fahrzeug.setPreis(Double.parseDouble(preisField.getText()));
                    
                    manager.updateFahrzeug(fahrzeug);
                    refreshTable();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Bitte geben Sie einen gültigen Preis ein.",
                        "Eingabefehler",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            dialog.add(saveButton);
            dialog.add(new JButton("Abbrechen") {{ addActionListener(e -> dialog.dispose()); }});
            
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
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
            
            JDialog dialog = new JDialog(this, "Fahrtenbuch - " + fahrzeug.getKennzeichen(), true);
            dialog.setLayout(new BorderLayout(5, 5));
            
            // Fahrtenliste
            List<FahrtenbuchEintrag> fahrten = fahrzeugService.getFahrtenForFahrzeug(fahrzeug.getKennzeichen());
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (FahrtenbuchEintrag fahrt : fahrten) {
                listModel.addElement(String.format("%s: %s -> %s (%s km)",
                    fahrt.getDatumFormatted(),
                    fahrt.getStart(),
                    fahrt.getZiel(),
                    fahrt.getKilometer()));
            }
            JList<String> fahrtList = new JList<>(listModel);
            
            // Neue Fahrt Panel
            JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
            JTextField startField = new JTextField();
            JTextField zielField = new JTextField();
            JTextField kilometerField = new JTextField();
            JTextField fahrerField = new JTextField();
            
            inputPanel.add(new JLabel("Start:"));
            inputPanel.add(startField);
            inputPanel.add(new JLabel("Ziel:"));
            inputPanel.add(zielField);
            inputPanel.add(new JLabel("Kilometer:"));
            inputPanel.add(kilometerField);
            inputPanel.add(new JLabel("Fahrer:"));
            inputPanel.add(fahrerField);
            
            JButton addButton = new JButton("Fahrt hinzufügen");
            addButton.addActionListener(e -> {
                try {
                    FahrtenbuchEintrag eintrag = new FahrtenbuchEintrag(
                        LocalDate.now(),
                        startField.getText(),
                        zielField.getText(),
                        Double.parseDouble(kilometerField.getText()),
                        fahrzeug.getKennzeichen()
                    );
                    eintrag.setFahrer(fahrerField.getText());
                    
                    manager.addFahrt(fahrzeug.getKennzeichen(), eintrag);
                    listModel.addElement(String.format("%s: %s -> %s (%s km)",
                        eintrag.getDatumFormatted(),
                        eintrag.getStart(),
                        eintrag.getZiel(),
                        eintrag.getKilometer()));
                    
                    startField.setText("");
                    zielField.setText("");
                    kilometerField.setText("");
                    fahrerField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Bitte geben Sie eine gültige Kilometerzahl ein.",
                        "Eingabefehler",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            dialog.add(new JScrollPane(fahrtList), BorderLayout.CENTER);
            dialog.add(inputPanel, BorderLayout.SOUTH);
            dialog.add(addButton, BorderLayout.EAST);
            
            dialog.setSize(400, 500);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
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
