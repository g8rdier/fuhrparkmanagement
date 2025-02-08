package de.fuhrpark.ui;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrtenbuchService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.service.impl.FahrtenbuchServiceImpl;
import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.ui.model.FahrzeugTableModel;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.dialog.FahrzeugEditDialog;

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
    private final FahrtenbuchService fahrtenbuchService;
    private final FahrzeugTableModel tableModel;
    
    // UI Components
    private final JTable fahrzeugTable;
    private final JComboBox<FahrzeugTyp> fahrzeugTypComboBox;
    private final JTextField kennzeichenField;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JTextField preisField;

    public FuhrparkUI(FuhrparkManager manager, FahrzeugService fahrzeugService, 
                     FahrtenbuchService fahrtenbuchService) {
        super("Fuhrpark Verwaltung");
        this.manager = manager;
        this.fahrzeugService = fahrzeugService;
        this.fahrzeugFactory = new FahrzeugFactoryImpl();
        this.fahrtenbuchService = fahrtenbuchService;
        this.tableModel = new FahrzeugTableModel();
        
        // Setup UI
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Initialize components
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
        addButton.addActionListener(e -> addNewFahrzeug());
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

    private void addNewFahrzeug() {
        FahrzeugDialog dialog = new FahrzeugDialog(this, manager.getFahrzeugFactory());
        dialog.setVisible(true);
        
        Fahrzeug fahrzeug = dialog.getResult();
        if (fahrzeug != null) {
            manager.speichereFahrzeug(fahrzeug);
            refreshTable();
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
                refreshTable();
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
                refreshTable();
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
                FahrzeugService service = new FahrzeugServiceImpl();
                FahrtenbuchService fahrtenbuchService = new FahrtenbuchServiceImpl();
                FuhrparkManager manager = new FuhrparkManager(service, new FahrzeugFactoryImpl());
                
                new FuhrparkUI(manager, service, fahrtenbuchService).setVisible(true);
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
