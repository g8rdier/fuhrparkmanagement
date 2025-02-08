package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import javax.swing.*;
import java.awt.*;

public class FahrzeugEditDialog extends JDialog {
    private final JTextField markeField;
    private final JTextField modellField;
    private final JTextField preisField;
    private boolean confirmed = false;
    private final Fahrzeug fahrzeug;

    public FahrzeugEditDialog(Window owner, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", ModalityType.APPLICATION_MODAL);
        this.fahrzeug = fahrzeug;
        
        markeField = new JTextField(20);
        modellField = new JTextField(20);
        preisField = new JTextField(20);

        initComponents();
        loadFahrzeugData();
    }

    private void loadFahrzeugData() {
        markeField.setText(fahrzeug.getMarke());
        modellField.setText(fahrzeug.getModell());
        preisField.setText(String.valueOf(fahrzeug.getPreis()));
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Show kennzeichen and type as read-only
        addFormField(inputPanel, "Kennzeichen:", new JLabel(fahrzeug.getKennzeichen()), gbc, 0);
        addFormField(inputPanel, "Typ:", new JLabel(fahrzeug.getClass().getSimpleName()), gbc, 1);
        
        // Editable fields
        addFormField(inputPanel, "Marke: *", markeField, gbc, 2);
        addFormField(inputPanel, "Modell:", modellField, gbc, 3);
        addFormField(inputPanel, "Preis (€): *", preisField, gbc, 4);

        // Add required fields note with smaller font
        JLabel requiredNote = new JLabel("* Pflichtfeld");
        requiredNote.setForeground(Color.RED);
        requiredNote.setFont(requiredNote.getFont().deriveFont(10.0f));  // Make font smaller
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        inputPanel.add(requiredNote, gbc);

        // Button panel
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

        // Add panels to dialog
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Configure dialog
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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