package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import javax.swing.*;
import java.awt.*;

public class FahrzeugDialog extends JDialog {
    private Fahrzeug result = null;
    private final JTextField kennzeichenField = new JTextField(10);
    private final JComboBox<FahrzeugTyp> typComboBox = new JComboBox<>(FahrzeugTyp.values());
    private final JTextField herstellerField = new JTextField(20);
    private final JTextField modellField = new JTextField(20);
    private final JTextField baujahrField = new JTextField(4);
    private final JTextField kilometerstandField = new JTextField(10);

    public FahrzeugDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Kennzeichen:"));
        inputPanel.add(kennzeichenField);
        inputPanel.add(new JLabel("Typ:"));
        inputPanel.add(typComboBox);
        inputPanel.add(new JLabel("Hersteller:"));
        inputPanel.add(herstellerField);
        inputPanel.add(new JLabel("Modell:"));
        inputPanel.add(modellField);
        inputPanel.add(new JLabel("Baujahr:"));
        inputPanel.add(baujahrField);
        inputPanel.add(new JLabel("Kilometerstand:"));
        inputPanel.add(kilometerstandField);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        saveButton.addActionListener(_ -> {
            if (validateInput()) {
                try {
                    result = new Fahrzeug(
                        kennzeichenField.getText(),
                        herstellerField.getText(),
                        modellField.getText(),
                        (FahrzeugTyp) typComboBox.getSelectedItem(),
                        Integer.parseInt(baujahrField.getText()),
                        Double.parseDouble(kilometerstandField.getText())
                    );
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Bitte geben Sie gültige Zahlen für Baujahr und Kilometerstand ein.",
                        "Eingabefehler",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(_ -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    private boolean validateInput() {
        if (kennzeichenField.getText().trim().isEmpty()) {
            showValidationError("Bitte geben Sie ein Kennzeichen ein.");
            return false;
        }
        if (herstellerField.getText().trim().isEmpty()) {
            showValidationError("Bitte geben Sie einen Hersteller ein.");
            return false;
        }
        if (modellField.getText().trim().isEmpty()) {
            showValidationError("Bitte geben Sie ein Modell ein.");
            return false;
        }
        if (baujahrField.getText().trim().isEmpty()) {
            showValidationError("Bitte geben Sie ein Baujahr ein.");
            return false;
        }
        if (kilometerstandField.getText().trim().isEmpty()) {
            showValidationError("Bitte geben Sie einen Kilometerstand ein.");
            return false;
        }
        return true;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message,
            "Validierungsfehler",
            JOptionPane.ERROR_MESSAGE);
    }

    public Fahrzeug getResult() {
        return result;
    }
} 