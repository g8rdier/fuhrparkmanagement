package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;

public class FuhrparkUI extends JFrame {
    // Constants
    private static final String[] VEHICLE_TYPES = {"PKW", "LKW"};
    private static final String LOCATION_PATTERN = "[A-ZÄÖÜ]{1,3}";
    private static final String LETTERS_PATTERN = "[A-Z]{1,2}";
    private static final String NUMBERS_PATTERN = "[1-9][0-9]{0,3}";
    
    // UI Components
    private final JComboBox<String> fahrzeugTypComboBox;
    private final JTextField markeField;
    private final JTextField modelField;
    private final JTextField licensePlateField;
    private final JList<String> vehicleList;
    private final DefaultListModel<String> listModel;
    
    private JButton editButton;
    private JButton deleteButton;
    
    private static class PlateDocument extends javax.swing.text.PlainDocument {
        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                throws javax.swing.text.BadLocationException {
            if (str == null) return;
            
            // Get current content and create new text
            String currentText = getText(0, getLength());
            String newText = new StringBuilder(currentText).insert(offs, str.toUpperCase()).toString();
            
            // Remove any existing separators
            String cleaned = newText.replace("-", "").replace(" ", "");
            
            // Check maximum length (8 chars without separators)
            if (cleaned.length() > 8) return;
            
            // Format the text
            StringBuilder formatted = new StringBuilder();
            int districtEnd = -1;
            
            // Find the end of the district code (1-3 letters)
            for (int i = 0; i < cleaned.length() && i < 3; i++) {
                char c = cleaned.charAt(i);
                if (!Character.isLetter(c)) {
                    break;
                }
                districtEnd = i;
            }
            
            if (districtEnd == -1) return;
            
            // Add district code
            formatted.append(cleaned.substring(0, districtEnd + 1));
            
            // Add separator after district code
            if (districtEnd + 1 < cleaned.length()) {
                formatted.append('-');
            }
            
            // Add remaining characters
            if (districtEnd + 1 < cleaned.length()) {
                String remaining = cleaned.substring(districtEnd + 1);
                int letterCount = 0;
                
                // Add up to 2 letters
                for (int i = 0; i < remaining.length() && letterCount < 2; i++) {
                    char c = remaining.charAt(i);
                    if (Character.isLetter(c)) {
                        formatted.append(c);
                        letterCount++;
                    } else {
                        break;
                    }
                }
                
                // Add space before numbers if we have numbers
                if (letterCount > 0 && remaining.length() > letterCount) {
                    formatted.append(' ');
                }
                
                // Add remaining numbers and optional H/E
                String numbers = remaining.substring(letterCount);
                if (!numbers.isEmpty()) {
                    // Validate numbers and H/E suffix
                    if (numbers.matches("[1-9][0-9]{0,3}[HE]?")) {
                        formatted.append(numbers);
                    }
                }
            }
            
            // Replace entire content with formatted text
            super.remove(0, getLength());
            super.insertString(0, formatted.toString(), a);
        }
    }
    
    public static boolean isValidLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.isEmpty()) {
            return false;
        }

        // Basic format check (with flexible separators)
        String cleaned = licensePlate.replace("-", "").replace(" ", "").toUpperCase();
        
        // Use the defined patterns for validation
        String fullPattern = LOCATION_PATTERN + LETTERS_PATTERN + NUMBERS_PATTERN + "[HE]?";
        return cleaned.matches(fullPattern);
    }
    
    public FuhrparkUI() {
        // Basic window setup
        setTitle("Fuhrpark Verwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize components
        fahrzeugTypComboBox = new JComboBox<>(VEHICLE_TYPES);
        markeField = new JTextField();
        modelField = new JTextField();
        licensePlateField = createLicensePlateField();
        listModel = new DefaultListModel<>();
        vehicleList = new JList<>(listModel);
        
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Input fields
        addInputField("Fahrzeugtyp:", fahrzeugTypComboBox, gbc, 0);
        addInputField("Marke:", markeField, gbc, 1);
        addInputField("Modell:", modelField, gbc, 2);
        addInputField("Kennzeichen:", licensePlateField, gbc, 3);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editButton = new JButton("Bearbeiten");
        deleteButton = new JButton("Löschen");
        JButton addButton = new JButton("Fahrzeug hinzufügen");
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);
        
        // Vehicle list
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Fahrzeuge"));
        add(scrollPane, gbc);
        
        // Add button listeners
        addButton.addActionListener(e -> addVehicle());
        editButton.addActionListener(e -> editVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        
        // Set minimum window size
        setMinimumSize(new Dimension(400, 500));
    }
    
    private void addInputField(String label, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        if (component instanceof JTextField) {
            ((JTextField) component).setPreferredSize(new Dimension(150, 25));
        }
        add(component, gbc);
    }
    
    private void addVehicle() {
        String type = (String) fahrzeugTypComboBox.getSelectedItem();
        String brand = markeField.getText().trim();
        String model = modelField.getText().trim();
        String licensePlate = licensePlateField.getText().trim();
        
        // Validation
        if (brand.isEmpty() || model.isEmpty() || !isValidLicensePlate(licensePlate)) {
            showValidationError();
            return;
        }
        
        // Add to list
        String vehicleEntry = String.format("%s [%s] %s %s", type, licensePlate, brand, model);
        listModel.addElement(vehicleEntry);
        
        // Clear fields
        clearFields();
    }
    
    private void editVehicle() {
        int selectedIndex = vehicleList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String entry = listModel.getElementAt(selectedIndex);
        // Parse entry (format: "TYPE [PLATE] BRAND MODEL")
        String type = entry.substring(0, entry.indexOf('[') - 1);
        String plate = entry.substring(entry.indexOf('[') + 1, entry.indexOf(']'));
        String rest = entry.substring(entry.indexOf(']') + 2);
        String[] parts = rest.split(" ", 2);
        String brand = parts[0];
        String model = parts[1];

        VehicleEditDialog dialog = new VehicleEditDialog(this, type, brand, model, plate);
        dialog.setVisible(true);

        if (dialog.isApproved()) {
            String newType = dialog.getSelectedType();
            String newBrand = dialog.getBrand();
            String newModel = dialog.getModel();
            String newPlate = dialog.getLicensePlate();

            // Update list entry
            String newEntry = String.format("%s [%s] %s %s", 
                newType, newPlate, newBrand, newModel);
            listModel.setElementAt(newEntry, selectedIndex);
        }
    }
    
    private void deleteVehicle() {
        int selectedIndex = vehicleList.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
        }
    }
    
    private void clearFields() {
        markeField.setText("");
        modelField.setText("");
        licensePlateField.setText("");
        fahrzeugTypComboBox.setSelectedIndex(0);
    }
    
    private void showValidationError() {
        JOptionPane.showMessageDialog(this,
            "Bitte füllen Sie alle Felder korrekt aus.",
            "Ungültige Eingabe",
            JOptionPane.ERROR_MESSAGE);
    }
    
    public JTextField createLicensePlateField() {
        JTextField field = new JTextField();
        field.setDocument(new PlateDocument());
        return field;
    }
    
    // Make the method public so VehicleEditDialog can access it
    public boolean isLicensePlateInUse(String licensePlate, int excludeIndex) {
        for (int i = 0; i < listModel.size(); i++) {
            if (i == excludeIndex) continue;
            String entry = listModel.getElementAt(i);
            // Extract license plate from entry (format: "TYPE [PLATE] BRAND MODEL")
            String plate = entry.substring(entry.indexOf('[') + 1, entry.indexOf(']'));
            if (plate.equals(licensePlate)) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FuhrparkUI().setVisible(true);
        });
    }
} 
