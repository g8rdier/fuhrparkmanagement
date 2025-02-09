package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.ui.util.KennzeichenFormatter;
import de.fuhrpark.manager.FuhrparkManager;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.NumberFormat;
import java.util.Locale;

public class FahrzeugDialog extends JDialog {
    private final JComboBox<String> typComboBox;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JFormattedTextField kennzeichenField;
    private final JFormattedTextField wertField;
    private boolean confirmed = false;

    // Constructor for new vehicle
    public FahrzeugDialog(JFrame owner) {
        super(owner, "Fahrzeug hinzufügen", true);
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = createKennzeichenField();
        this.wertField = createWertField();
        initComponents();
    }

    // Constructor for editing existing vehicle
    public FahrzeugDialog(JFrame owner, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = createKennzeichenField();
        this.wertField = createWertField();
        
        // Set existing values
        typComboBox.setSelectedItem(fahrzeug instanceof de.fuhrpark.model.impl.PKW ? "PKW" : "LKW");
        markeField.setText(fahrzeug.getMarke());
        modellField.setText(fahrzeug.getModell());
        kennzeichenField.setText(fahrzeug.getKennzeichen());
        wertField.setValue(fahrzeug.getWert());
        
        // Disable type and kennzeichen for existing vehicles
        typComboBox.setEnabled(false);
        kennzeichenField.setEnabled(false);
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components in correct order
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Typ:"), gbc);
        gbc.gridx = 1;
        panel.add(typComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Marke:"), gbc);
        gbc.gridx = 1;
        panel.add(markeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Modell:"), gbc);
        gbc.gridx = 1;
        panel.add(modellField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Kennzeichen:"), gbc);
        gbc.gridx = 1;
        panel.add(kennzeichenField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Aktueller Wert (€):"), gbc);
        gbc.gridx = 1;
        panel.add(wertField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        okButton.addActionListener(e -> {
            if (validateInputs()) {
                confirmed = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    private JFormattedTextField createKennzeichenField() {
        try {
            MaskFormatter kennzeichenMask = new MaskFormatter("UUU-UU####");
            kennzeichenMask.setPlaceholderCharacter('_');
            kennzeichenMask.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
            JFormattedTextField field = new JFormattedTextField(kennzeichenMask);
            field.setColumns(20);
            return field;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private JFormattedTextField createWertField() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        JFormattedTextField field = new JFormattedTextField(currencyFormat);
        field.setColumns(20);
        field.setValue(0.00);
        return field;
    }

    private boolean validateInputs() {
        if (markeField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return false;
        }
        if (modellField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Modell ein.");
            return false;
        }
        String kennzeichen = getKennzeichen();
        if (kennzeichen.isEmpty() || kennzeichen.contains("_")) {
            showError("Bitte geben Sie ein vollständiges Kennzeichen ein.");
            return false;
        }
        try {
            Number wert = (Number) wertField.getValue();
            if (wert.doubleValue() <= 0) {
                showError("Der Wert muss größer als 0 sein.");
                return false;
            }
        } catch (Exception e) {
            showError("Bitte geben Sie einen gültigen Wert ein.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    public boolean showDialog() {
        confirmed = false;
        setVisible(true);
        return confirmed;
    }

    public String getSelectedType() {
        return (String) typComboBox.getSelectedItem();
    }

    public String getMarke() {
        return markeField.getText().trim();
    }

    public String getModell() {
        return modellField.getText().trim();
    }

    public String getKennzeichen() {
        return kennzeichenField.getText().trim();
    }

    public double getWert() {
        try {
            Number value = (Number) wertField.getValue();
            return value.doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
} 