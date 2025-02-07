package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.FahrzeugTyp;
import javax.swing.*;
import java.awt.*;

public class FahrzeugDialog extends JDialog {
    private Fahrzeug result = null;
    private final JTextField kennzeichenField = new JTextField(10);
    private final JComboBox<FahrzeugTyp> typComboBox = new JComboBox<>(FahrzeugTyp.values());
    private final JTextField herstellerField = new JTextField(20);
    private final JTextField modellField = new JTextField(20);

    public FahrzeugDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Kennzeichen:"));
        inputPanel.add(kennzeichenField);
        inputPanel.add(new JLabel("Typ:"));
        inputPanel.add(typComboBox);
        inputPanel.add(new JLabel("Hersteller:"));
        inputPanel.add(herstellerField);
        inputPanel.add(new JLabel("Modell:"));
        inputPanel.add(modellField);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                result = new Fahrzeug(
                    kennzeichenField.getText(),
                    (FahrzeugTyp) typComboBox.getSelectedItem(),
                    herstellerField.getText(),
                    modellField.getText()
                );
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
            JOptionPane.showMessageDialog(this, 
                "Bitte geben Sie ein Kennzeichen ein.",
                "Validierungsfehler",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (herstellerField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Bitte geben Sie einen Hersteller ein.",
                "Validierungsfehler",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (modellField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Bitte geben Sie ein Modell ein.",
                "Validierungsfehler",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public Fahrzeug getResult() {
        return result;
    }
} 