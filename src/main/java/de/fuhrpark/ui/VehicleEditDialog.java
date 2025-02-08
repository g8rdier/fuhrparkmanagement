package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;

public class VehicleEditDialog extends JDialog {
    private static final String[] VEHICLE_TYPES = {"PKW", "LKW"};
    
    private final JComboBox<String> typeComboBox;
    private final JTextField brandField;
    private final JTextField modelField;
    private final JTextField licensePlateField;
    private final JTextField priceField;
    private boolean approved = false;
    
    public VehicleEditDialog(Frame owner, String type, String brand, String model, String licensePlate, String price) {
        super(owner, "Fahrzeug bearbeiten", true);
        
        // Initialize components
        typeComboBox = new JComboBox<>(VEHICLE_TYPES);
        typeComboBox.setSelectedItem(type);
        
        brandField = new JTextField(brand, 20);
        modelField = new JTextField(model, 20);
        licensePlateField = new JTextField(licensePlate, 20);
        priceField = new JTextField(price, 20);
        priceField.setDocument(new FuhrparkUI.PriceDocument());
        
        initComponents();
        pack();
        setLocationRelativeTo(owner);
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add input fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Fahrzeugtyp:"), gbc);
        gbc.gridx = 1;
        add(typeComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Marke:"), gbc);
        gbc.gridx = 1;
        add(brandField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Modell:"), gbc);
        gbc.gridx = 1;
        add(modelField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Kennzeichen:"), gbc);
        gbc.gridx = 1;
        add(licensePlateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Kaufpreis (€):"), gbc);
        gbc.gridx = 1;
        add(priceField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");
        
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                approved = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);
        
        // Set minimum size
        setMinimumSize(new Dimension(300, 200));
    }
    
    private boolean validateInput() {
        if (brandField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return false;
        }
        if (modelField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Modell ein.");
            return false;
        }
        if (!isValidLicensePlate(licensePlateField.getText().trim())) {
            showError("Bitte geben Sie ein gültiges Kennzeichen ein.");
            return false;
        }
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Ungültige Eingabe",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean isValidLicensePlate(String plate) {
        // Use the same validation as in FuhrparkUI
        if (plate == null || plate.isEmpty()) return false;
        String cleaned = plate.replace("-", "").replace(" ", "").toUpperCase();
        return cleaned.matches("[A-ZÄÖÜ]{1,3}[A-Z]{1,2}[1-9][0-9]{0,3}[HE]?");
    }
    
    public boolean isApproved() {
        return approved;
    }
    
    public String getSelectedType() {
        return (String) typeComboBox.getSelectedItem();
    }
    
    public String getBrand() {
        return brandField.getText().trim();
    }
    
    public String getModel() {
        return modelField.getText().trim();
    }
    
    public String getLicensePlate() {
        return licensePlateField.getText().trim();
    }
    
    public String getPrice() {
        return priceField.getText().trim();
    }
} 