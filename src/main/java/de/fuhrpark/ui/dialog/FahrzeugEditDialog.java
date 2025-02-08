package de.fuhrpark.ui.dialog;

import de.fuhrpark.ui.model.VehicleData;
import javax.swing.*;
import java.awt.*;

public class VehicleEditDialog extends JDialog {
    private final JComboBox<String> typeComboBox;
    private final JTextField brandField;
    private final JTextField modelField;
    private final JTextField licensePlateField;
    private final JTextField priceField;
    private boolean confirmed = false;

    public VehicleEditDialog(JFrame parent, VehicleData data) {
        super(parent, "Fahrzeug bearbeiten", true);
        
        // Initialize components with only PKW and LKW
        typeComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        brandField = new JTextField(20);
        modelField = new JTextField(20);
        licensePlateField = new JTextField(20);
        priceField = new JTextField(20);

        // Set initial values
        typeComboBox.setSelectedItem(data.getType());
        brandField.setText(data.getBrand());
        modelField.setText(data.getModel());
        licensePlateField.setText(data.getLicensePlate());
        priceField.setText(data.getPrice());

        // Layout
        setLayout(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components with required field markers
        addFormField(inputPanel, "Fahrzeugtyp: *", typeComboBox, gbc, 0);
        addFormField(inputPanel, "Marke: *", brandField, gbc, 1);
        addFormField(inputPanel, "Modell:", modelField, gbc, 2);
        addFormField(inputPanel, "Kennzeichen: *", licensePlateField, gbc, 3);
        addFormField(inputPanel, "Kaufpreis (€): *", priceField, gbc, 4);

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
        setLocationRelativeTo(parent);
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

    public String getVehicleType() {
        return (String) typeComboBox.getSelectedItem();
    }

    public String getBrand() {
        return brandField.getText();
    }

    public String getModel() {
        return modelField.getText();
    }

    public String getLicensePlate() {
        return licensePlateField.getText();
    }

    public String getPrice() {
        return priceField.getText();
    }

    private boolean validateInputs() {
        if (brandField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return false;
        }
        if (licensePlateField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Kennzeichen ein.");
            return false;
        }
        if (priceField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie einen Kaufpreis ein.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
} 