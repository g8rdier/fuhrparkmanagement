package de.fuhrpark.ui;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.LKW;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.repository.impl.FileDataStore;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.dialog.FahrzeugEditDialog;
import de.fuhrpark.ui.model.FahrzeugTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final FuhrparkManager manager;
    private final FahrzeugFactory fahrzeugFactory;
    private final FahrzeugTableModel tableModel;
    private final JTable fahrzeugTable;

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
        
        initializeUI();
        refreshTable();
    }

    private void initializeUI() {
        // Set minimum size and layout
        setMinimumSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // Create toolbar with buttons
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JButton addButton = new JButton("Fahrzeug hinzufügen");
        JButton editButton = new JButton("Fahrzeug bearbeiten");
        JButton deleteButton = new JButton("Fahrzeug löschen");

        addButton.addActionListener(this::showAddDialog);
        editButton.addActionListener(this::showEditDialog);
        deleteButton.addActionListener(this::deleteSelectedFahrzeug);

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);

        // Setup table
        fahrzeugTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fahrzeugTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);

        // Add components to frame
        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Center on screen
        setLocationRelativeTo(null);
        pack();
    }

    private void showAddDialog(ActionEvent e) {
        addFahrzeug();
    }

    private void showEditDialog(ActionEvent e) {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = fahrzeugTable.convertRowIndexToModel(selectedRow);
            String kennzeichen = (String) tableModel.getValueAt(modelRow, 1);
            Fahrzeug fahrzeug = manager.getFahrzeug(kennzeichen);
            
            if (fahrzeug != null) {
                FahrzeugEditDialog dialog = new FahrzeugEditDialog(this, fahrzeug);
                dialog.setVisible(true);
                
                if (dialog.isConfirmed() && dialog.updateFahrzeug()) {
                    refreshTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedFahrzeug(ActionEvent e) {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = fahrzeugTable.convertRowIndexToModel(selectedRow);
            String kennzeichen = (String) tableModel.getValueAt(modelRow, 1); // Assuming kennzeichen is in column 1
            manager.deleteFahrzeug(kennzeichen);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshTable() {
        List<Fahrzeug> fahrzeuge = manager.getAlleFahrzeuge();
        tableModel.setFahrzeuge(fahrzeuge);
        tableModel.fireTableDataChanged();
    }

    private void addFahrzeug() {
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

                // Create the appropriate vehicle type with proper class references
                if ("PKW".equals(typ)) {
                    fahrzeug = new PKW(kennzeichen, marke, modell, wert);
                } else {
                    fahrzeug = new LKW(kennzeichen, marke, modell, wert);
                }

                // Add to model and update table
                tableModel.addFahrzeug(fahrzeug);
                tableModel.fireTableDataChanged();
                
                // Optional: Select the newly added vehicle
                int newRow = tableModel.getRowCount() - 1;
                if (newRow >= 0) {
                    fahrzeugTable.setRowSelectionInterval(newRow, newRow);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Fehler beim Hinzufügen des Fahrzeugs: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
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
