package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import javax.swing.*;
import java.awt.*;

public class FahrzeugEditDialog extends JDialog {
    private final JTextField markeField;
    private final JTextField modellField;
    private final JTextField preisField;
    private boolean confirmed = false;
    private final Fahrzeug fahrzeug;

    public FahrzeugEditDialog(JFrame owner, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        this.fahrzeug = fahrzeug;
        
        markeField = new JTextField(20);
        modellField = new JTextField(20);
        preisField = new JTextField(20);

        initComponents();
        loadFahrzeugData();
        setLocationRelativeTo(owner);
    }

    private void loadFahrzeugData() {
        markeField.setText(fahrzeug.getMarke());
        modellField.setText(fahrzeug.getModell());
        preisField.setText(String.valueOf(fahrzeug.getPreis()));
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Read-only fields
        addFormField(panel, "Kennzeichen:", new JLabel(fahrzeug.getKennzeichen()), gbc, 0);
        addFormField(panel, "Typ:", new JLabel(fahrzeug.getClass().getSimpleName()), gbc, 1);
        
        // Editable fields
        addFormField(panel, "Marke: *", markeField, gbc, 2);
        addFormField(panel, "Modell:", modellField, gbc, 3);
        addFormField(panel, "Preis (€): *", preisField, gbc, 4);

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
            fahrzeug.setMarke(markeField.getText().trim());
            fahrzeug.setModell(modellField.getText().trim());
            fahrzeug.setPreis(Double.parseDouble(preisField.getText().trim()));
            return true;
        } catch (Exception e) {
            showError("Fehler beim Speichern: " + e.getMessage());
            return false;
        }
    }

    private boolean validateInputs() {
        if (markeField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return false;
        }
        if (preisField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie einen Kaufpreis ein.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
} 