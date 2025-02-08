package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import de.fuhrpark.model.LKW;
import de.fuhrpark.service.FahrzeugFactory;
import javax.swing.*;
import java.awt.*;

public class FahrzeugDialog extends JDialog {
    private Fahrzeug result = null;
    private final JTextField kennzeichenField = new JTextField(10);
    private final JTextField markeField = new JTextField(10);
    private final JTextField modellField = new JTextField(10);
    private final JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});

    public FahrzeugDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Kennzeichen:"));
        inputPanel.add(kennzeichenField);
        inputPanel.add(new JLabel("Marke:"));
        inputPanel.add(markeField);
        inputPanel.add(new JLabel("Modell:"));
        inputPanel.add(modellField);
        inputPanel.add(new JLabel("Typ:"));
        inputPanel.add(typeComboBox);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                result = createFahrzeug(FahrzeugFactory.getInstance());
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

    private Fahrzeug createFahrzeug(FahrzeugFactory factory) {
        String typ = (String) typeComboBox.getSelectedItem();
        String kennzeichen = kennzeichenField.getText();
        String marke = markeField.getText();
        String modell = modellField.getText();

        if ("PKW".equals(typ)) {
            return factory.erstelleFahrzeug("PKW", kennzeichen, marke, modell, 5, true);
        } else {
            return factory.erstelleFahrzeug("LKW", kennzeichen, marke, modell, 7.5, false);
        }
    }

    public Fahrzeug getResult() {
        return result;
    }
} 