package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.text.ParseException;

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
        licensePlateField = new JTextField();
        licensePlateField.setDocument(new LicensePlateDocument());
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
        String priceText = priceField.getText().trim();

        // Basic validation
        if (brand.isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return;
        }
        if (model.isEmpty()) {
            showError("Bitte geben Sie ein Modell ein.");
            return;
        }
        if (licensePlate.isEmpty()) {
            showError("Bitte geben Sie ein Kennzeichen ein.");
            return;
        }

        // Price validation
        try {
            // Parse price using German locale to handle dots and commas correctly
            NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMANY);
            Number number = format.parse(priceText);
            double priceValue = number.doubleValue();
            
            if (priceValue <= 0) {
                showError("Der Kaufpreis muss größer als 0 sein.");
                return;
            }

            // Format price for display
            String formattedPrice = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(priceValue);
            
            // Add to list with formatted price
            String vehicleEntry = String.format("%s [%s] %s %s - %s",
                type, licensePlate, brand, model, formattedPrice);
            listModel.addElement(vehicleEntry);
            
            clearFields();
            
        } catch (ParseException e) {
            showError("Bitte geben Sie einen gültigen Kaufpreis ein.");
            return;
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
            try {
                NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMANY);
                Number number = format.parse(dialog.getPrice());
                double price = number.doubleValue();
                
                if (price <= 0) {
                    showError("Der Kaufpreis muss größer als 0 sein.");
                    return;
                }

                String formattedPrice = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(price);
                
                String newEntry = String.format("%s [%s] %s %s - %s",
                    dialog.getType(), dialog.getLicensePlate(), 
                    dialog.getBrand(), dialog.getModel(), formattedPrice);
                listModel.setElementAt(newEntry, selectedIndex);
                
            } catch (ParseException e) {
                showError("Bitte geben Sie einen gültigen Kaufpreis ein.");
                return;
            }
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
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
    }
    
    public JTextField createLicensePlateField() {
        JTextField field = new JTextField();
        field.setDocument(new LicensePlateDocument());
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
    
    private static class LicensePlateDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) return;
            
            String currentText = getText(0, getLength());
            String newText = new StringBuilder(currentText).insert(offs, str.toUpperCase()).toString();
            
            // Remove all spaces and dashes for validation
            String cleaned = newText.replace("-", "").replace(" ", "");
            
            // Maximum length check (including separators)
            if (cleaned.length() > 8) return;
            
            StringBuilder formatted = new StringBuilder();
            int pos = 0;
            
            // District code (1-3 letters)
            while (pos < cleaned.length() && pos < 3 && Character.isLetter(cleaned.charAt(pos))) {
                formatted.append(cleaned.charAt(pos));
                pos++;
            }
            
            if (pos == 0) return; // Must have at least one letter
            
            // Add separator if there's more
            if (pos < cleaned.length()) {
                formatted.append('-');
            }
            
            // Recognition letters (1-2 letters)
            int letterCount = 0;
            while (pos < cleaned.length() && letterCount < 2 && Character.isLetter(cleaned.charAt(pos))) {
                formatted.append(cleaned.charAt(pos));
                pos++;
                letterCount++;
            }
            
            // Numbers (1-4 digits)
            if (pos < cleaned.length()) {
                // Add space before numbers if we had recognition letters
                if (letterCount > 0) {
                    formatted.append(' ');
                }
                
                StringBuilder numbers = new StringBuilder();
                while (pos < cleaned.length() && Character.isDigit(cleaned.charAt(pos))) {
                    numbers.append(cleaned.charAt(pos));
                    pos++;
                }
                
                // Check if numbers are valid (1-4 digits, no leading zero)
                if (numbers.length() > 0 && numbers.charAt(0) != '0' && numbers.length() <= 4) {
                    formatted.append(numbers);
                    
                    // Optional H or E suffix
                    if (pos < cleaned.length() && (cleaned.charAt(pos) == 'H' || cleaned.charAt(pos) == 'E')) {
                        formatted.append(cleaned.charAt(pos));
                    }
                }
            }
            
            // Replace entire content with formatted text
            super.remove(0, getLength());
            super.insertString(0, formatted.toString(), a);
        }
    }
} 
