package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import javax.swing.*;
import java.awt.*;

public class FahrzeugEditDialog extends JDialog {
    private final JLabel kennzeichenLabel;
    private final JLabel typLabel;
    private final JLabel markeLabel;
    private final JLabel modellLabel;
    private final JTextField preisField;
    private boolean confirmed = false;
    private final Fahrzeug fahrzeug;

    public FahrzeugEditDialog(JFrame owner, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        this.fahrzeug = fahrzeug;
        
        // Initialize components
        this.kennzeichenLabel = new JLabel();
        this.typLabel = new JLabel();
        this.markeLabel = new JLabel();
        this.modellLabel = new JLabel();
        this.preisField = new JTextField(20);

        initComponents();
        loadFahrzeugData();
        setLocationRelativeTo(owner);
    }

    private void loadFahrzeugData() {
        kennzeichenLabel.setText(fahrzeug.getKennzeichen());
        typLabel.setText(fahrzeug.getClass().getSimpleName());
        markeLabel.setText(fahrzeug.getMarke());
        modellLabel.setText(fahrzeug.getModell());
        preisField.setText(String.format("%.2f", fahrzeug.getPreis()));
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add components in correct order with proper labels
        addFormField(panel, "Kennzeichen:", kennzeichenLabel, gbc, 0);
        addFormField(panel, "Typ:", typLabel, gbc, 1);
        addFormField(panel, "Marke:", markeLabel, gbc, 2);
        addFormField(panel, "Modell:", modellLabel, gbc, 3);
        addFormField(panel, "Preis (€): *", preisField, gbc, 4);

        // Style read-only labels
        kennzeichenLabel.setForeground(Color.DARK_GRAY);
        typLabel.setForeground(Color.DARK_GRAY);
        markeLabel.setForeground(Color.DARK_GRAY);
        modellLabel.setForeground(Color.DARK_GRAY);

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
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean updateFahrzeug() {
        if (!validateInputs()) {
            return false;
        }
        
        try {
            fahrzeug.setPreis(Double.parseDouble(preisField.getText().trim()));
            return true;
        } catch (Exception e) {
            showError("Fehler beim Speichern: " + e.getMessage());
            return false;
        }
    }

    private boolean validateInputs() {
        if (preisField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie einen Kaufpreis ein.");
            return false;
        }
        try {
            double preis = Double.parseDouble(preisField.getText().trim());
            if (preis <= 0) {
                showError("Der Preis muss größer als 0 sein.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Bitte geben Sie einen gültigen Preis ein.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
} 