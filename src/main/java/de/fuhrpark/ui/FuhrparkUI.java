package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

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
    private final JTextField priceField;
    private final JList<String> vehicleList;
    private final DefaultListModel<String> listModel;
    
    private JButton editButton;
    private JButton deleteButton;
    
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
        priceField = new JTextField();
        priceField.setDocument(new PriceDocument());
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
        addInputField("Kaufpreis (€):", priceField, gbc, 4);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editButton = new JButton("Bearbeiten");
        deleteButton = new JButton("Löschen");
        JButton addButton = new JButton("Fahrzeug hinzufügen");
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);
        
        // Vehicle list
        gbc.gridy = 6;
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
        String price = priceField.getText().trim();
        
        // Validation
        if (brand.isEmpty() || model.isEmpty() || !isValidLicensePlate(licensePlate)) {
            showValidationError();
            return;
        }
        
        if (price.isEmpty()) {
            showError("Bitte geben Sie einen Kaufpreis ein.");
            return;
        }
        
        try {
            double priceValue = Double.parseDouble(price);
            if (priceValue <= 0) {
                showError("Der Kaufpreis muss größer als 0 sein.");
                return;
            }
            
            // Format price with two decimal places and thousands separator
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            String formattedPrice = formatter.format(priceValue);
            
            // Add to list with price
            String vehicleEntry = String.format("%s [%s] %s %s - %s",
                type, licensePlate, brand, model, formattedPrice);
            listModel.addElement(vehicleEntry);
            
            clearFields();
            
        } catch (NumberFormatException e) {
            showError("Bitte geben Sie einen gültigen Kaufpreis ein.");
        }
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
        // Parse entry (format: "TYPE [PLATE] BRAND MODEL - PRICE")
        String type = entry.substring(0, entry.indexOf('[') - 1);
        String plate = entry.substring(entry.indexOf('[') + 1, entry.indexOf(']'));
        String rest = entry.substring(entry.indexOf(']') + 2);
        String[] parts = rest.split(" - ", 2);
        String brand = parts[0];
        String model = parts[1];
        String price = parts[2];

        VehicleEditDialog dialog = new VehicleEditDialog(this, type, brand, model, plate, price);
        dialog.setVisible(true);

        if (dialog.isApproved()) {
            String newType = dialog.getSelectedType();
            String newBrand = dialog.getBrand();
            String newModel = dialog.getModel();
            String newPlate = dialog.getLicensePlate();
            String newPrice = dialog.getPrice();

            // Update list entry
            String newEntry = String.format("%s [%s] %s %s - %s", 
                newType, newPlate, newBrand, newModel, newPrice);
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
        priceField.setText("");
        fahrzeugTypComboBox.setSelectedIndex(0);
    }
    
    private void showValidationError() {
        JOptionPane.showMessageDialog(this,
            "Bitte füllen Sie alle Felder korrekt aus.",
            "Ungültige Eingabe",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
    }
    
    public JTextField createLicensePlateField() {
        JTextField field = new JTextField();
        return field;
    }
    
    // Make the method public so VehicleEditDialog can access it
    public boolean isLicensePlateInUse(String licensePlate, int excludeIndex) {
        for (int i = 0; i < listModel.size(); i++) {
            if (i == excludeIndex) continue;
            String entry = listModel.getElementAt(i);
            // Extract license plate from entry (format: "TYPE [PLATE] BRAND MODEL - PRICE")
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
