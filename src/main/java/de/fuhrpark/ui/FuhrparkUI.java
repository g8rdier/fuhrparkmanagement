package de.fuhrpark.ui;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.persistence.repository.impl.FileDataStore;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.dialog.FahrzeugEditDialog;
import de.fuhrpark.ui.model.FahrzeugTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final FuhrparkManager manager;
    private final FahrzeugFactory fahrzeugFactory;
    private final FahrzeugTableModel tableModel;
    private final JTable fahrzeugTable;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;

    public FuhrparkUI(FileDataStore dataStore) {
        super("Fuhrpark Verwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize services and manager
        FahrzeugService fahrzeugService = new FahrzeugServiceImpl(dataStore);
        this.fahrzeugFactory = new FahrzeugFactoryImpl();
        this.manager = new FuhrparkManager(fahrzeugService, fahrzeugFactory);
        
        // Initialize UI components
        this.tableModel = new FahrzeugTableModel();
        this.fahrzeugTable = new JTable(tableModel);
        this.addButton = new JButton("Hinzufügen");
        this.editButton = new JButton("Bearbeiten");
        this.deleteButton = new JButton("Löschen");
        
        // Setup table
        fahrzeugTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableRowSorter<FahrzeugTableModel> sorter = new TableRowSorter<>(tableModel);
        fahrzeugTable.setRowSorter(sorter);

        // Layout
        setLayout(new BorderLayout());
        
        // Table panel
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(this::addFahrzeug);
        editButton.addActionListener(this::editFahrzeug);
        deleteButton.addActionListener(this::deleteFahrzeug);

        // Set window properties
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void addFahrzeug(ActionEvent e) {
        FahrzeugDialog dialog = new FahrzeugDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                Fahrzeug fahrzeug;
                String typ = dialog.getSelectedType();
                String marke = dialog.getMarke();
                String modell = dialog.getModell();
                String kennzeichen = dialog.getKennzeichen();
                double wert = dialog.getWert();

                // Create the appropriate vehicle type
                if ("PKW".equals(typ)) {
                    fahrzeug = new PKW(kennzeichen, marke, modell, wert);
                } else {
                    fahrzeug = new LKW(kennzeichen, marke, modell, wert);
                }

                tableModel.addFahrzeug(fahrzeug);
                
                int newRow = tableModel.getRowCount() - 1;
                if (newRow >= 0) {
                    fahrzeugTable.setRowSelectionInterval(newRow, newRow);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Fehler beim Hinzufügen des Fahrzeugs: " + ex.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editFahrzeug(ActionEvent e) {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedRow = fahrzeugTable.convertRowIndexToModel(selectedRow);
            Fahrzeug fahrzeug = tableModel.getFahrzeug(selectedRow);
            FahrzeugDialog dialog = new FahrzeugDialog(this, fahrzeug);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                tableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            }
        }
    }

    private void deleteFahrzeug(ActionEvent e) {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedRow = fahrzeugTable.convertRowIndexToModel(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Möchten Sie das ausgewählte Fahrzeug wirklich löschen?",
                "Fahrzeug löschen",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeFahrzeug(selectedRow);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create components with proper error handling
                FileDataStore dataStore = new FileDataStore();
                dataStore.load();
                
                new FuhrparkUI(dataStore).setVisible(true);
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
