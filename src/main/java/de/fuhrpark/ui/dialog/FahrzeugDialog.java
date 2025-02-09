package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import javax.swing.*;
import java.awt.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class FahrzeugDialog extends JDialog {
    private final JComboBox<String> typComboBox;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JTextField kennzeichenField;
    private final JFormattedTextField preisField;
    private boolean confirmed = false;

    // Constructor for new vehicle
    public FahrzeugDialog(JFrame owner) {
        super(owner, "Neues Fahrzeug", true);
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = new JTextField(20);
        this.preisField = createPreisField();
        initComponents();
    }

    // Constructor for editing existing vehicle
    public FahrzeugDialog(JFrame owner, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(fahrzeug.getMarke(), 20);
        this.modellField = new JTextField(fahrzeug.getModell(), 20);
        this.kennzeichenField = new JTextField(fahrzeug.getKennzeichen(), 20);
        this.preisField = createPreisField();
        this.preisField.setValue(fahrzeug.getPreis());
        
        // Disable type selection and license plate for existing vehicles
        this.typComboBox.setSelectedItem(fahrzeug.getTyp());
        this.typComboBox.setEnabled(false);
        this.kennzeichenField.setEnabled(false);
        
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
        panel.add(new JLabel("Aktueller Preis (€):"), gbc);
        gbc.gridx = 1;
        panel.add(preisField, gbc);

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

    private JFormattedTextField createPreisField() {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(2);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        return new JFormattedTextField(formatter);
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
            Number preis = (Number) preisField.getValue();
            if (preis.doubleValue() <= 0) {
                showError("Der Preis muss größer als 0 sein.");
                return false;
            }
        } catch (Exception e) {
            showError("Bitte geben Sie einen gültigen Preis ein.");
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

    public double getPreis() {
        try {
            Number value = (Number) preisField.getValue();
            return value != null ? value.doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
} 