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
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final FuhrparkManager manager;
    private final FahrzeugFactory fahrzeugFactory;
    private final FahrzeugTableModel tableModel;
    private final JTable fahrzeugTable;

    public FuhrparkUI(FileDataStore dataStore) {
        // Initialize services
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
        setTitle("Fuhrpark Verwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create toolbar with buttons
        JToolBar toolbar = new JToolBar();
        JButton addButton = new JButton("Fahrzeug hinzufügen");
        JButton deleteButton = new JButton("Fahrzeug löschen");

        toolbar.add(addButton);
        toolbar.add(deleteButton);

        // Add action listeners
        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> deleteSelectedFahrzeug());

        // Add components to frame
        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(fahrzeugTable), BorderLayout.CENTER);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void showAddDialog() {
        FahrzeugDialog dialog = new FahrzeugDialog(this, fahrzeugFactory);
        if (dialog.showDialog()) {
            manager.addFahrzeug(
                dialog.getSelectedTyp(),
                dialog.getKennzeichen(),
                dialog.getMarke(),
                dialog.getModell(),
                dialog.getPreis()
            );
            refreshTable();
        }
    }

    private void deleteSelectedFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            String kennzeichen = (String) tableModel.getValueAt(selectedRow, 1); // Assuming kennzeichen is in column 1
            manager.deleteFahrzeug(kennzeichen);
            refreshTable();
        }
    }

    private void refreshTable() {
        List<Fahrzeug> fahrzeuge = manager.getAlleFahrzeuge();
        tableModel.updateFahrzeuge(fahrzeuge);
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
