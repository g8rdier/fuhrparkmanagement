package de.fuhrpark.ui;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.persistence.repository.impl.FileDataStore;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.model.FahrzeugTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        JButton deleteButton = new JButton("Fahrzeug löschen");

        addButton.addActionListener(this::showAddDialog);
        deleteButton.addActionListener(this::deleteSelectedFahrzeug);

        toolbar.add(addButton);
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
        FahrzeugDialog dialog = new FahrzeugDialog(this, fahrzeugFactory);
        if (dialog.showDialog()) {
            refreshTable();
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
        tableModel.setFahrzeuge(manager.getAlleFahrzeuge());
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
