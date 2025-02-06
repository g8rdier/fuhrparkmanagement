package de.fuhrpark.ui;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.service.FahrtenbuchService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FahrtenbuchDialog extends JDialog {
    private final JTable table;
    private final String kennzeichen;
    private final FahrtenbuchService service;

    public FahrtenbuchDialog(Frame owner, String kennzeichen, FahrtenbuchService service) {
        super(owner, "Fahrtenbuch: " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.service = service;

        // Create toolbar with New button
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton("Neu");
        newButton.addActionListener(e -> showNewEntryDialog());
        toolBar.add(newButton);

        // Table setup
        table = new JTable(new FahrtenbuchTableModel(service.getEintraegeForFahrzeug(kennzeichen)));
        
        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    private void showNewEntryDialog() {
        JDialog dialog = new JDialog(this, "Neue Fahrt", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField startField = new JTextField();
        JTextField zielField = new JTextField();
        JTextField kilometerField = new JTextField();
        JTextField fahrerField = new JTextField();

        panel.add(new JLabel("Start:"));
        panel.add(startField);
        panel.add(new JLabel("Ziel:"));
        panel.add(zielField);
        panel.add(new JLabel("Kilometer:"));
        panel.add(kilometerField);
        panel.add(new JLabel("Fahrer:"));
        panel.add(fahrerField);

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> {
            try {
                double kilometer = Double.parseDouble(kilometerField.getText());
                service.addFahrtenbuchEintrag(new FahrtenbuchEintrag(
                    LocalDate.now(),
                    startField.getText(),
                    zielField.getText(),
                    kilometer,
                    kennzeichen
                ));
                ((FahrtenbuchTableModel)table.getModel()).updateData(
                    service.getEintraegeForFahrzeug(kennzeichen)
                );
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Bitte geben Sie eine gültige Kilometerzahl ein.",
                    "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
} 