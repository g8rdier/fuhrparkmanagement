package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.FahrtenbuchEintrag;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import java.util.ArrayList;

public class FuhrparkUI extends JFrame {
    private final JLabel kennzeichenLabel;
    private final JLabel markeLabel;
    private final JLabel modellLabel;
    private final JLabel typLabel;
    private final JLabel baujahrLabel;
    private final JLabel kilometerstandLabel;
    private final JTable reparaturenTable;
    private final JTextField searchField;
    private final FahrzeugService fahrzeugService;
    private final FahrtenbuchService fahrtenbuchService;
    private final ReparaturService reparaturService;

    public FuhrparkUI(FahrzeugService fahrzeugService, 
                      FahrtenbuchService fahrtenbuchService,
                      ReparaturService reparaturService) {
        this.fahrzeugService = fahrzeugService;
        this.fahrtenbuchService = fahrtenbuchService;
        this.reparaturService = reparaturService;
        
        setTitle("Fuhrpark Verwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize labels
        kennzeichenLabel = new JLabel("Kennzeichen: ");
        markeLabel = new JLabel("Marke: ");
        modellLabel = new JLabel("Modell: ");
        typLabel = new JLabel("Typ: ");
        baujahrLabel = new JLabel("Baujahr: ");
        kilometerstandLabel = new JLabel("Kilometerstand: ");

        // Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(6, 1));
        detailsPanel.add(kennzeichenLabel);
        detailsPanel.add(markeLabel);
        detailsPanel.add(modellLabel);
        detailsPanel.add(typLabel);
        detailsPanel.add(baujahrLabel);
        detailsPanel.add(kilometerstandLabel);

        // Reparaturen Table
        String[] columnNames = {"Beschreibung", "Kosten", "Werkstatt", "Datum"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        reparaturenTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reparaturenTable);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Fahrzeug suchen");
        searchPanel.add(new JLabel("Kennzeichen:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add search functionality
        searchButton.addActionListener(e -> {
            searchFahrzeug();
        });

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        JButton fahrtenbuchButton = new JButton("Fahrtenbuch öffnen");
        JButton addReparaturButton = new JButton("Reparatur hinzufügen");
        JButton newFahrzeugButton = new JButton("Neues Fahrzeug");
        
        // Add button functionality
        fahrtenbuchButton.addActionListener(e -> {
            String kennzeichen = getCurrentKennzeichen();
            if (kennzeichen != null) {
                FahrtenbuchDialog dialog = new FahrtenbuchDialog(this, kennzeichen, fahrtenbuchService);
                dialog.setVisible(true);
            }
        });
        addReparaturButton.addActionListener(e -> {
            handleAddReparatur();
        });
        newFahrzeugButton.addActionListener(e -> {
            FahrzeugDialog dialog = new FahrzeugDialog(this, "Neues Fahrzeug", true);
            dialog.setVisible(true);
            
            Fahrzeug newFahrzeug = dialog.getResult();
            if (newFahrzeug != null) {
                fahrzeugService.addFahrzeug(newFahrzeug);
                searchField.setText(newFahrzeug.getKennzeichen());
                searchFahrzeug();
            }
        });
        
        buttonsPanel.add(fahrtenbuchButton);
        buttonsPanel.add(addReparaturButton);
        buttonsPanel.add(newFahrzeugButton);

        // Layout assembly
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(detailsPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void updateFahrzeugDetails(Fahrzeug fahrzeug) {
        if (fahrzeug != null) {
            kennzeichenLabel.setText(fahrzeug.getKennzeichen());
            markeLabel.setText(fahrzeug.getMarke());
            modellLabel.setText(fahrzeug.getModell());
            typLabel.setText(fahrzeug.getTyp().toString());
            baujahrLabel.setText(String.valueOf(fahrzeug.getBaujahr()));
            kilometerstandLabel.setText(String.format("%.0f", fahrzeug.getKilometerstand()));

            // Update tables
            List<ReparaturBuchEintrag> reparaturen = reparaturService.getReparaturenForFahrzeug(fahrzeug.getKennzeichen());
            updateReparaturenTable(reparaturen);
            
            List<FahrtenbuchEintrag> fahrten = fahrtenbuchService.getEintraegeForFahrzeug(fahrzeug.getKennzeichen());
            updateFahrtenList(fahrten);
        } else {
            clearLabels();
        }
    }

    private void searchFahrzeug() {
        String kennzeichen = searchField.getText().trim();
        Fahrzeug fahrzeug = fahrzeugService.getFahrzeugByKennzeichen(kennzeichen);
        updateFahrzeugDetails(fahrzeug);
    }

    private void clearLabels() {
        kennzeichenLabel.setText("");
        markeLabel.setText("");
        modellLabel.setText("");
        typLabel.setText("");
        baujahrLabel.setText("");
        kilometerstandLabel.setText("");
        // Clear tables
        updateReparaturenTable(new ArrayList<>());
        updateFahrtenList(new ArrayList<>());
    }

    public void updateReparaturenTable(List<ReparaturBuchEintrag> reparaturen) {
        DefaultTableModel model = (DefaultTableModel) reparaturenTable.getModel();
        model.setRowCount(0);
        for (ReparaturBuchEintrag reparatur : reparaturen) {
            model.addRow(new Object[]{
                reparatur.getBeschreibung(),
                reparatur.getKosten(),
                reparatur.getWerkstatt(),
                reparatur.getDatumFormatted()
            });
        }
    }

    private void handleAddReparatur() {
        String kennzeichen = getCurrentKennzeichen();
        if (kennzeichen != null) {
            ReparaturDialog dialog = new ReparaturDialog(this, kennzeichen);
            dialog.setVisible(true);
            
            ReparaturBuchEintrag eintrag = dialog.getResult();
            if (eintrag != null) {
                reparaturService.addReparatur(kennzeichen, eintrag);
                updateFahrzeugDetails(fahrzeugService.getFahrzeug(kennzeichen));
            }
        }
    }

    private String getCurrentKennzeichen() {
        Fahrzeug selectedFahrzeug = getCurrentFahrzeug();
        return selectedFahrzeug != null ? selectedFahrzeug.getKennzeichen() : null;
    }

    private Fahrzeug getCurrentFahrzeug() {
        String kennzeichen = searchField.getText();
        return fahrzeugService.getFahrzeug(kennzeichen);
    }

    private void updateFahrtenList(List<FahrtenbuchEintrag> fahrten) {
        // Add code here to display the fahrtenbuch entries in your UI
        // For example, you might want to add another JTable or list to show them
    }
} 
