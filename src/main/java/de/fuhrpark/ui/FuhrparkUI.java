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
    private final JLabel baujahrlabel;
    private final JLabel kilometerstandLabel;
    private final JTable reparaturenTable;
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
        baujahrlabel = new JLabel("Baujahr: ");
        kilometerstandLabel = new JLabel("Kilometerstand: ");

        // Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(6, 1));
        detailsPanel.add(kennzeichenLabel);
        detailsPanel.add(markeLabel);
        detailsPanel.add(modellLabel);
        detailsPanel.add(typLabel);
        detailsPanel.add(baujahrlabel);
        detailsPanel.add(kilometerstandLabel);

        // Reparaturen Table
        String[] columnNames = {"Beschreibung", "Kosten", "Werkstatt", "Datum"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        reparaturenTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reparaturenTable);

        // Add a search panel
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Fahrzeug suchen");
        searchPanel.add(new JLabel("Kennzeichen:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String kennzeichen = searchField.getText();
            Fahrzeug fahrzeug = fahrzeugService.getFahrzeugByKennzeichen(kennzeichen);
            updateFahrzeugDetails(fahrzeug);
            if (fahrzeug != null) {
                List<ReparaturBuchEintrag> reparaturen = reparaturService.getReparaturen(kennzeichen);
                updateReparaturenTable(reparaturen);
            }
        });

        // Create buttons panel
        JPanel buttonsPanel = new JPanel();
        JButton fahrtenbuchButton = new JButton("Fahrtenbuch öffnen");
        fahrtenbuchButton.addActionListener(e -> {
            String kennzeichen = searchField.getText();
            if (!kennzeichen.isEmpty()) {
                FahrtenbuchDialog dialog = new FahrtenbuchDialog(this, kennzeichen, fahrtenbuchService);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        });
        buttonsPanel.add(fahrtenbuchButton);

        // Add all panels to the frame
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(detailsPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set a reasonable initial size
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public void updateFahrzeugDetails(Fahrzeug fahrzeug) {
        if (fahrzeug != null) {
            kennzeichenLabel.setText("Kennzeichen: " + fahrzeug.getKennzeichen());
            markeLabel.setText("Marke: " + fahrzeug.getMarke());
            modellLabel.setText("Modell: " + fahrzeug.getModell());
            typLabel.setText("Typ: " + fahrzeug.getTyp());
            baujahrlabel.setText("Baujahr: " + fahrzeug.getBaujahr());
            kilometerstandLabel.setText("Kilometerstand: " + fahrzeug.getKilometerstand());
        } else {
            clearFahrzeugDetails();
        }
    }

    private void clearFahrzeugDetails() {
        kennzeichenLabel.setText("Kennzeichen: ");
        markeLabel.setText("Marke: ");
        modellLabel.setText("Modell: ");
        typLabel.setText("Typ: ");
        baujahrlabel.setText("Baujahr: ");
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
} 