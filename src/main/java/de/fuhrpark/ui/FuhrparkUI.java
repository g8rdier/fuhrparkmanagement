package de.fuhrpark.ui;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.model.FahrzeugTableModel;
import javax.swing.*;
import java.awt.*;

public class FuhrparkUI extends JFrame {
    private final FahrzeugFactory fahrzeugFactory;
    private final JTable fahrzeugTable;
    private final FahrzeugTableModel tableModel;

    public FuhrparkUI(FahrzeugFactory fahrzeugFactory) {
        super("Fuhrpark Verwaltung");
        this.fahrzeugFactory = fahrzeugFactory;
        this.tableModel = new FahrzeugTableModel();
        this.fahrzeugTable = new JTable(tableModel);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Create toolbar with buttons
        JToolBar toolbar = new JToolBar();
        JButton addButton = new JButton("Fahrzeug hinzufügen");
        JButton editButton = new JButton("Fahrzeug bearbeiten");
        JButton deleteButton = new JButton("Fahrzeug löschen");
        
        addButton.addActionListener(e -> addFahrzeug());
        editButton.addActionListener(e -> editFahrzeug());
        deleteButton.addActionListener(e -> deleteFahrzeug());
        
        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        add(toolbar, BorderLayout.NORTH);

        // Add table in a scroll pane
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        add(scrollPane, BorderLayout.CENTER);

        // Set preferred size
        setPreferredSize(new Dimension(800, 600));
    }

    private void addFahrzeug() {
        FahrzeugDialog dialog = new FahrzeugDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                Fahrzeug fahrzeug = fahrzeugFactory.createFahrzeug(
                    dialog.getSelectedType(),
                    dialog.getKennzeichen(),
                    dialog.getMarke(),
                    dialog.getModell(),
                    dialog.getPreis()
                );

                tableModel.addFahrzeug(fahrzeug);
                tableModel.fireTableDataChanged();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Fehler beim Hinzufügen: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Fahrzeug fahrzeug = tableModel.getFahrzeug(selectedRow);
        FahrzeugDialog dialog = new FahrzeugDialog(this, fahrzeug);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                // Update the vehicle with new values
                fahrzeug.setMarke(dialog.getMarke());
                fahrzeug.setModell(dialog.getModell());
                fahrzeug.setPreis(dialog.getPreis());
                
                tableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Fehler beim Bearbeiten: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Möchten Sie das ausgewählte Fahrzeug wirklich löschen?",
            "Fahrzeug löschen",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeFahrzeug(selectedRow);
            tableModel.fireTableDataChanged();
        }
    }
} 
