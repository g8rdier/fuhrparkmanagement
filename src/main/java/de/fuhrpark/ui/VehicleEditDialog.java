package de.fuhrpark.ui;

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
        
        // Initialize components
        typeComboBox = new JComboBox<>(new String[]{"PKW", "LKW", "Motorrad"});
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

        // Add components
        addFormField(inputPanel, "Fahrzeugtyp:", typeComboBox, gbc, 0);
        addFormField(inputPanel, "Marke:", brandField, gbc, 1);
        addFormField(inputPanel, "Modell:", modelField, gbc, 2);
        addFormField(inputPanel, "Kennzeichen:", licensePlateField, gbc, 3);
        addFormField(inputPanel, "Kaufpreis (€):", priceField, gbc, 4);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
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
} 