package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.enums.ReparaturTyp;
import de.fuhrpark.service.ReparaturService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ReparaturDialog extends JDialog {
    private final JTable table;
    private final String kennzeichen;
    private final ReparaturService service;

    public ReparaturDialog(Frame owner, String kennzeichen, ReparaturService service) {
        super(owner, "Reparaturen: " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.service = service;

        // Create toolbar with New button
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton("Neu");
        newButton.addActionListener(e -> showNewEntryDialog());
        toolBar.add(newButton);

        // Table setup
        table = new JTable(new ReparaturTableModel(service.getReparaturenForFahrzeug(kennzeichen)));
        
        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    private void showNewEntryDialog() {
        JDialog dialog = new JDialog(this, "Neue Reparatur", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JComboBox<ReparaturTyp> typCombo = new JComboBox<>(ReparaturTyp.values());
        JTextField beschreibungField = new JTextField();
        JTextField kostenField = new JTextField();
        JTextField werkstattField = new JTextField();

        panel.add(new JLabel("Typ:"));
        panel.add(typCombo);
        panel.add(new JLabel("Beschreibung:"));
        panel.add(beschreibungField);
        panel.add(new JLabel("Kosten:"));
        panel.add(kostenField);
        panel.add(new JLabel("Werkstatt:"));
        panel.add(werkstattField);

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> {
            try {
                double kosten = Double.parseDouble(kostenField.getText());
                service.addReparatur(new ReparaturBuchEintrag(
                    LocalDate.now(),
                    (ReparaturTyp)typCombo.getSelectedItem(),
                    beschreibungField.getText(),
                    kosten,
                    kennzeichen,
                    werkstattField.getText()
                ));
                ((ReparaturTableModel)table.getModel()).updateData(
                    service.getReparaturenForFahrzeug(kennzeichen)
                );
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Bitte geben Sie gültige Kosten ein.",
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