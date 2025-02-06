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
    private final JTextField startOrtField = new JTextField(20);
    private final JTextField zielOrtField = new JTextField(20);
    private final JTextField kilometerField = new JTextField(20);
    private final JTextField kennzeichenField = new JTextField(20);
    private FahrtenbuchEintrag result = null;

    public FahrtenbuchDialog(Frame owner, String kennzeichen, FahrtenbuchService service) {
        super(owner, "Fahrtenbuch: " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.service = service;

        // Create toolbar with New button
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton("Neu");
        newButton.addActionListener(_ -> showNewEntryDialog());
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

        addComponent(panel, new JLabel("Start:"), startOrtField, 0);
        addComponent(panel, new JLabel("Ziel:"), zielOrtField, 1);
        addComponent(panel, new JLabel("Kilometer:"), kilometerField, 2);
        addComponent(panel, new JLabel("Kennzeichen:"), kennzeichenField, 3);

        // Button panel with fixed lambda parameters
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        okButton.addActionListener(_ -> {  // Use underscore for unused parameter
            if (isInputValid()) {
                result = createFahrtenbuchEintrag();
                service.addEintrag(result);
                ((FahrtenbuchTableModel)table.getModel()).updateData(
                    service.getEintraegeForFahrzeug(kennzeichen)
                );
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Bitte füllen Sie alle Felder aus.", 
                    "Ungültige Eingabe", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(_ -> dialog.dispose());  // Use underscore for unused parameter

        panel.add(buttonPanel);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addComponent(JPanel panel, JLabel label, JComponent field, int row) {
        panel.add(label, new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panel.add(field, new GridBagConstraints(1, row, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    private boolean isInputValid() {
        return !startOrtField.getText().trim().isEmpty() &&
               !zielOrtField.getText().trim().isEmpty() &&
               !kilometerField.getText().trim().isEmpty() &&
               !kennzeichenField.getText().trim().isEmpty();
    }

    private FahrtenbuchEintrag createFahrtenbuchEintrag() {
        try {
            return new FahrtenbuchEintrag(
                LocalDate.now(),
                startOrtField.getText().trim(),
                zielOrtField.getText().trim(),
                Double.parseDouble(kilometerField.getText().trim()),
                kennzeichenField.getText().trim()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Bitte geben Sie eine gültige Kilometerzahl ein.",
                "Ungültige Eingabe",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public FahrtenbuchEintrag getResult() {
        return result;
    }
} 