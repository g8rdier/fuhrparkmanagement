package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugFactory;
import javax.swing.*;
import java.awt.*;

public class FahrzeugDialog extends JDialog {
    private Fahrzeug result = null;
    private final JTextField kennzeichenField = new JTextField(10);
    private final JTextField markeField = new JTextField(10);
    private final JTextField modellField = new JTextField(10);
    private final JTextField preisField = new JTextField(10);
    private final JComboBox<FahrzeugTyp> typeComboBox = new JComboBox<>(FahrzeugTyp.values());
    private final FahrzeugFactory fahrzeugFactory;

    public FahrzeugDialog(Frame owner, FahrzeugFactory factory) {
        super(owner, "Fahrzeug hinzufügen", true);
        if (factory == null) {
            throw new IllegalArgumentException("FahrzeugFactory darf nicht null sein");
        }
        this.fahrzeugFactory = factory;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Kennzeichen:"));
        inputPanel.add(kennzeichenField);
        inputPanel.add(new JLabel("Marke:"));
        inputPanel.add(markeField);
        inputPanel.add(new JLabel("Modell:"));
        inputPanel.add(modellField);
        inputPanel.add(new JLabel("Preis:"));
        inputPanel.add(preisField);
        inputPanel.add(new JLabel("Typ:"));
        inputPanel.add(typeComboBox);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                result = createFahrzeug();
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

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
        return true;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message,
            "Validierungsfehler",
            JOptionPane.ERROR_MESSAGE);
    }

    private Fahrzeug createFahrzeug() {
        try {
            FahrzeugTyp typ = (FahrzeugTyp) typeComboBox.getSelectedItem();
            String kennzeichen = kennzeichenField.getText().trim();
            String marke = markeField.getText().trim();
            String modell = modellField.getText().trim();
            double preis = Double.parseDouble(preisField.getText().trim());

            return fahrzeugFactory.erstelleFahrzeug(typ, marke, modell, kennzeichen, preis);
        } catch (NumberFormatException e) {
            showValidationError("Bitte geben Sie einen gültigen Preis ein.");
            return null;
        }
    }

    public Fahrzeug getResult() {
        return result;
    }
} 