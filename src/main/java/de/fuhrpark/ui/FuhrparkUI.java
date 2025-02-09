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
    private final FahrtenbuchService fahrtenbuchService;
    private final FahrzeugTableModel tableModel;
    private final JTable fahrzeugTable;
    
    public FuhrparkUI(DataStore dataStore) {
        super("Fuhrpark Verwaltung");
        
        // Initialize services
        FahrzeugService fahrzeugService = new FahrzeugServiceImpl(dataStore);
        FahrzeugFactory fahrzeugFactory = new FahrzeugFactoryImpl();
        this.fahrtenbuchService = new FahrtenbuchServiceImpl(dataStore);
        
        // Initialize manager
        this.manager = new FuhrparkManager(fahrzeugService, fahrzeugFactory, dataStore);
        
        // Initialize UI components
        this.tableModel = new FahrzeugTableModel();
        this.fahrzeugTable = new JTable(tableModel);

        initializeUI();
        updateTableData();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        
        // Add toolbar with main actions
        JToolBar toolbar = createToolbar();
        add(toolbar, BorderLayout.NORTH);
        
        // Add table with scrolling
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Set minimum size and pack
        setMinimumSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JButton addButton = new JButton("Neu");
        JButton editButton = new JButton("Bearbeiten");
        JButton deleteButton = new JButton("Löschen");
        JButton fahrtenbuchButton = new JButton("Fahrtenbuch");

        addButton.addActionListener(e -> addNewFahrzeug());
        editButton.addActionListener(e -> editSelectedFahrzeug());
        deleteButton.addActionListener(e -> deleteSelectedFahrzeug());
        fahrtenbuchButton.addActionListener(e -> showFahrtenbuch());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.addSeparator();
        toolbar.add(fahrtenbuchButton);

        return toolbar;
    }

    private void updateTableData() {
        List<Fahrzeug> fahrzeuge = manager.getAlleFahrzeuge();
        tableModel.updateFahrzeuge(fahrzeuge);
    }

    private void addNewFahrzeug() {
        FahrzeugDialog dialog = new FahrzeugDialog(this, manager.getFahrzeugFactory());
        dialog.setVisible(true);
        
        if (dialog.getResult() != null) {
            manager.addFahrzeug(dialog.getResult());
            updateTableData();
        }
    }

    private void editSelectedFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            Fahrzeug fahrzeug = tableModel.getRow(selectedRow);
            FahrzeugEditDialog dialog = new FahrzeugEditDialog(this, fahrzeug);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed() && dialog.updateFahrzeug()) {
                manager.updateFahrzeug(fahrzeug);
                updateTableData();
            }
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
                updateTableData();
            }
        }
    }

    private void showFahrtenbuch() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            Fahrzeug fahrzeug = tableModel.getRow(selectedRow);
            List<FahrtenbuchEintrag> fahrten = 
                fahrtenbuchService.getFahrtenForFahrzeug(fahrzeug.getKennzeichen());
            
            JDialog dialog = new JDialog(this, "Fahrtenbuch - " + fahrzeug.getKennzeichen(), true);
            dialog.setLayout(new BorderLayout(5, 5));
            
            // Fahrtenliste
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
                    
                    addFahrt(fahrzeug.getKennzeichen(), eintrag);
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

    private void addFahrt(String kennzeichen, FahrtenbuchEintrag eintrag) {
        fahrtenbuchService.addFahrt(kennzeichen, eintrag);
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
