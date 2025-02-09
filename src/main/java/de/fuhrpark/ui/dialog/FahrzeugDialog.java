package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.ui.util.KennzeichenFormatter;
import de.fuhrpark.manager.FuhrparkManager;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
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
    private final FuhrparkManager manager;

    public FahrzeugDialog(Frame owner, FuhrparkManager manager) {
        super(owner, "Fahrzeug hinzufügen", true);
        this.manager = manager;
        
        // Initialize components in correct order
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = new JFormattedTextField();
        kennzeichenField.setDocument(new KennzeichenFormatter());
        
        // Setup currency formatter for wertField
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        this.wertField = new JFormattedTextField(currencyFormat);
        wertField.setColumns(20);
        wertField.setValue(0.00);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components in correct order
        addFormField(panel, "Typ:", typComboBox, gbc, 0);
        addFormField(panel, "Marke:", markeField, gbc, 1);
        addFormField(panel, "Modell:", modellField, gbc, 2);
        addFormField(panel, "Kennzeichen:", kennzeichenField, gbc, 3);
        addFormField(panel, "Aktueller Wert (€):", wertField, gbc, 4);

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

    private boolean validateInputs() {
        if (markeField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return false;
        }
        if (modellField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Modell ein.");
            return false;
        }
        if (kennzeichenField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Kennzeichen ein.");
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
        Number value = (Number) wertField.getValue();
        return value.doubleValue();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
} 