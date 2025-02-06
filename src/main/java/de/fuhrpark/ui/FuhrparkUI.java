package de.fuhrpark.ui;

import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
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
        JDialog dialog = new JDialog(this, "Neues Fahrzeug", true);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));

        JTextField kennzeichenField = new JTextField();
        JTextField markeField = new JTextField();
        JTextField modellField = new JTextField();
        JComboBox<FahrzeugTyp> typCombo = new JComboBox<>(FahrzeugTyp.values());
        JTextField baujahrField = new JTextField();
        JTextField wertField = new JTextField();

        dialog.add(new JLabel("Kennzeichen:"));
        dialog.add(kennzeichenField);
        dialog.add(new JLabel("Marke:"));
        dialog.add(markeField);
        dialog.add(new JLabel("Modell:"));
        dialog.add(modellField);
        dialog.add(new JLabel("Typ:"));
        dialog.add(typCombo);
        dialog.add(new JLabel("Baujahr:"));
        dialog.add(baujahrField);
        dialog.add(new JLabel("Wert (€):"));
        dialog.add(wertField);

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> {
            try {
                Fahrzeug newFahrzeug = new Fahrzeug(
                    kennzeichenField.getText(),
                    markeField.getText(),
                    modellField.getText(),
                    (FahrzeugTyp) typCombo.getSelectedItem(),
                    Integer.parseInt(baujahrField.getText()),
                    Double.parseDouble(wertField.getText())
                );
                fahrzeugService.addFahrzeug(newFahrzeug);
                refreshTable();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Bitte gültige Zahlen für Baujahr und Wert eingeben.",
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

    private void showEditDialog() {
        // Will implement edit functionality
        JOptionPane.showMessageDialog(this, "Edit functionality coming soon!");
    }

    private void deleteSelectedFahrzeug() {
        // Will implement delete functionality
        JOptionPane.showMessageDialog(this, "Delete functionality coming soon!");
    }

    private void showLogbookView() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Fahrzeug aus.");
            return;
        }

        Fahrzeug fahrzeug = tableModel.getFahrzeugAt(selectedRow);
        List<FahrtenbuchEintrag> eintraege = 
            fahrtenbuchService.getEintraegeForFahrzeug(fahrzeug.getKennzeichen());

        JDialog dialog = new JDialog(this, "Fahrtenbuch: " + fahrzeug.getKennzeichen(), true);
        dialog.setLayout(new BorderLayout());
        
        // Create table model and table for logbook entries
        String[] columns = {"Datum", "Start", "Ziel", "Kilometer", "Fahrer"};
        Object[][] data = eintraege.stream()
            .map(e -> new Object[]{e.getDatum(), e.getStart(), e.getZiel(), 
                                 e.getKilometer(), e.getFahrer()})
            .toArray(Object[][]::new);

        JTable logTable = new JTable(data, columns);
        dialog.add(new JScrollPane(logTable), BorderLayout.CENTER);
        
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showRepairView() {
        // Will implement repair view
        JOptionPane.showMessageDialog(this, "Repair view coming soon!");
    }
} 