package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;

public class VehicleEditDialog extends JDialog {
    private final FuhrparkUI parent;
    private final int editingIndex;
    private JComboBox<String> vehicleTypeCombo;
    private JComboBox<String> brandCombo;
    private JTextField customBrandField;
    private JTextField modelField;
    private JTextField licensePlateField;
    private boolean saveClicked = false;
    
    public VehicleEditDialog(FuhrparkUI parent, String type, String brand, String model, String licensePlate, int editingIndex) {
        super(parent, "Fahrzeug bearbeiten", true);
        this.parent = parent;
        this.editingIndex = editingIndex;
        
        // Create main panel
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Vehicle type
        panel.add(new JLabel("Fahrzeugtyp:"));
        vehicleTypeCombo = new JComboBox<>(new String[]{"PKW", "LKW"});
        vehicleTypeCombo.setSelectedItem(type);
        panel.add(vehicleTypeCombo);
        
        // Brand
        panel.add(new JLabel("Marke:"));
        JPanel brandPanel = new JPanel(new BorderLayout());
        brandCombo = new JComboBox<>(FuhrparkUI.CAR_BRANDS);
        customBrandField = new JTextField();
        
        // Set brand
        if (java.util.Arrays.asList(FuhrparkUI.CAR_BRANDS).contains(brand)) {
            brandCombo.setSelectedItem(brand);
            customBrandField.setVisible(false);
        } else {
            brandCombo.setSelectedItem("Other");
            customBrandField.setText(brand);
            customBrandField.setVisible(true);
        }
        
        brandCombo.addActionListener(e -> {
            customBrandField.setVisible("Other".equals(brandCombo.getSelectedItem()));
        });
        
        brandPanel.add(brandCombo, BorderLayout.NORTH);
        brandPanel.add(customBrandField, BorderLayout.SOUTH);
        panel.add(brandPanel);
        
        // Model
        panel.add(new JLabel("Modell:"));
        modelField = new JTextField(model);
        panel.add(modelField);
        
        // License plate
        panel.add(new JLabel("Kennzeichen:"));
        licensePlateField = new JTextField(licensePlate);
        panel.add(licensePlateField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");
        
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                saveClicked = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add all to dialog
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private boolean validateInput() {
        String licensePlate = licensePlateField.getText().trim().toUpperCase();
        
        // First check if it's a valid format
        if (!FuhrparkUI.isValidLicensePlate(licensePlate)) {
            JOptionPane.showMessageDialog(this,
                "Bitte geben Sie ein gültiges Kennzeichen ein.\n\n" +
                "Format: XXX-XX 1234\n" +
                "Beispiele:\n" +
                "• B-AB 123\n" +
                "• M-XY 4567\n" +
                "• HH-AB 42",
                "Ungültiges Kennzeichen",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Then check for duplicates
        if (parent.isLicensePlateInUse(licensePlate, editingIndex)) {
            JOptionPane.showMessageDialog(this,
                "Ein Fahrzeug mit diesem Kennzeichen existiert bereits.",
                "Duplikat Kennzeichen",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    public boolean isSaveClicked() {
        return saveClicked;
    }
    
    public String getVehicleType() {
        return (String) vehicleTypeCombo.getSelectedItem();
    }
    
    public String getBrand() {
        String selectedBrand = (String) brandCombo.getSelectedItem();
        return "Other".equals(selectedBrand) ? customBrandField.getText().trim() : selectedBrand;
    }
    
    public String getModel() {
        return modelField.getText().trim();
    }
    
    public String getLicensePlate() {
        return licensePlateField.getText().trim().toUpperCase();
    }
} 