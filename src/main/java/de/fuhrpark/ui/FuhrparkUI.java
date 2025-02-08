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
    private final DefaultListModel<String> listModel;
    private final JList<String> vehicleList;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton addButton;
    
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
        // Initialize all fields in constructor before calling initComponents
        fahrzeugTypComboBox = new JComboBox<>(new String[]{"PKW", "LKW", "Motorrad"});
        markeField = new JTextField();
        modelField = new JTextField();
        licensePlateField = new JTextField();
        priceField = new JTextField();
        listModel = new DefaultListModel<>();
        vehicleList = new JList<>(listModel);
        editButton = new JButton("Bearbeiten");
        deleteButton = new JButton("Löschen");
        addButton = new JButton("Fahrzeug hinzufügen");

        initComponents();
    }
    
    private void initComponents() {
        setTitle("Fuhrpark Verwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Fahrzeugtyp:"), gbc);
        
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Marke:"), gbc);
        
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Modell:"), gbc);
        
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Kennzeichen:"), gbc);
        
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Kaufpreis (€):"), gbc);
        
        // Input fields
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        inputPanel.add(fahrzeugTypComboBox, gbc);
        
        gbc.gridy = 1;
        inputPanel.add(markeField, gbc);
        
        gbc.gridy = 2;
        inputPanel.add(modelField, gbc);
        
        gbc.gridy = 3;
        licensePlateField.setDocument(new LicensePlateDocument());
        inputPanel.add(licensePlateField, gbc);
        
        gbc.gridy = 4;
        priceField.setDocument(new PriceDocument());
        inputPanel.add(priceField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        
        // List panel
        JPanel listPanel = new JPanel(new BorderLayout(0, 5));
        listPanel.add(new JLabel("Fahrzeuge:"), BorderLayout.NORTH);
        
        vehicleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        addButton.addActionListener(e -> addVehicle());
        editButton.addActionListener(e -> editVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        
        // Initial button state
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        // Add list selection listener
        vehicleList.addListSelectionListener(e -> {
            boolean hasSelection = !vehicleList.isSelectionEmpty();
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
        
        pack();
        setLocationRelativeTo(null);
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
            NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMANY);
            Number number = format.parse(priceText);
            double priceValue = number.doubleValue();
            
            if (priceValue <= 0) {
                showError("Der Kaufpreis muss größer als 0 sein.");
                return;
            }

            String formattedPrice = NumberFormat.getCurrencyInstance(Locale.GERMANY)
                .format(priceValue);
            
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
