package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.PKW;
import javax.swing.*;
import java.awt.*;

public class FahrzeugDialog extends JDialog {
    private Fahrzeug result = null;
    private final JTextField kennzeichenField = new JTextField(10);

    public FahrzeugDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Kennzeichen:"));
        inputPanel.add(kennzeichenField);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                result = new PKW(kennzeichenField.getText());
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

    public Fahrzeug getResult() {
        return result;
    }
} 