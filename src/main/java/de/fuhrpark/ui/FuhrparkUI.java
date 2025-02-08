package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.ReparaturBuchEintrag;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;

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
        searchButton.addActionListener(_ -> {
            searchFahrzeug();
        });

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        JButton fahrtenbuchButton = new JButton("Fahrtenbuch öffnen");
        JButton addReparaturButton = new JButton("Reparatur hinzufügen");
        
        // Add button functionality
        fahrtenbuchButton.addActionListener(_ -> {
            String kennzeichen = getCurrentKennzeichen();
            if (kennzeichen != null) {
                FahrtenbuchDialog dialog = new FahrtenbuchDialog(this, kennzeichen, fahrtenbuchService);
                dialog.setVisible(true);
            }
        });
        addReparaturButton.addActionListener(_ -> {
            // Add reparatur logic
        });
        
        buttonsPanel.add(fahrtenbuchButton);
        buttonsPanel.add(addReparaturButton);

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
            // Use fahrzeugService to get and display vehicle details
            kennzeichenLabel.setText(fahrzeug.getKennzeichen());
            markeLabel.setText(fahrzeug.getMarke());
            modellLabel.setText(fahrzeug.getModell());
            typLabel.setText(fahrzeug.getTyp().toString());
            baujahrLabel.setText(String.valueOf(fahrzeug.getBaujahr()));
            kilometerstandLabel.setText(String.valueOf(fahrzeug.getKilometerstand()));
            
            // Use reparaturService to update repairs
            List<ReparaturBuchEintrag> reparaturen = reparaturService.getReparaturenForFahrzeug(fahrzeug.getKennzeichen());
            updateReparaturenTable(reparaturen);
            
            // Use fahrtenbuchService to update logbook if needed
            // ... any logbook-related updates
        } else {
            // Clear all labels if no vehicle found
            clearLabels();
        }
    }

    private void searchFahrzeug() {
        String kennzeichen = searchField.getText().trim();
        Fahrzeug fahrzeug = fahrzeugService.getFahrzeugByKennzeichen(kennzeichen);
        updateFahrzeugDetails(fahrzeug);
    }

    private void clearLabels() {
        kennzeichenLabel.setText("Kennzeichen: ");
        markeLabel.setText("Marke: ");
        modellLabel.setText("Modell: ");
        typLabel.setText("Typ: ");
        baujahrLabel.setText("Baujahr: ");
        kilometerstandLabel.setText("Kilometerstand: ");
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

    private String getCurrentKennzeichen() {
        String kennzeichen = searchField.getText().trim();
        if (kennzeichen.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Bitte geben Sie zuerst ein Kennzeichen ein.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return kennzeichen;
    }
} 
} 