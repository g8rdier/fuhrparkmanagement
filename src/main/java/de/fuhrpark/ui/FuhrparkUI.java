package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.ReparaturBuchEintrag;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final JLabel kennzeichenLabel;
    private final JLabel markeLabel;
    private final JLabel modellLabel;
    private final JLabel typLabel;
    private final JLabel baujahrlabel;
    private final JLabel kilometerstandLabel;
    private final JTable reparaturenTable;

    public FuhrparkUI() {
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

        // Add components to frame
        add(detailsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        pack();
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