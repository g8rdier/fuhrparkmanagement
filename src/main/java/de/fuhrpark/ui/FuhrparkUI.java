package de.fuhrpark.ui;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrtenbuchService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.persistence.repository.DataStore;
import de.fuhrpark.persistence.repository.impl.FileDataStore;
import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.ui.model.FahrzeugTableModel;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.dialog.FahrzeugEditDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final FuhrparkManager manager;
    private final FahrzeugTableModel tableModel;
    private final JTable fahrzeugTable;

    public FuhrparkUI(DataStore dataStore) {
        // Initialize services
        FahrzeugService fahrzeugService = new FahrzeugServiceImpl(dataStore);
        FahrzeugFactory fahrzeugFactory = new FahrzeugFactoryImpl();
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
        FahrzeugDialog dialog = new FahrzeugDialog(this);
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
            manager.loescheFahrzeug(kennzeichen);
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
