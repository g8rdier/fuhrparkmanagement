package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FuhrparkUI extends JFrame {
    private JComboBox<String> vehicleTypeCombo;
    private JComboBox<String> brandCombo;
    private JTextField customBrandField;
    private JTextField modelField;
    private JTextField licensePlateField;
    private JButton saveButton;
    private JList<String> vehicleList;
    private DefaultListModel<String> listModel;
    
    private JPanel logbookPanel;
    private JTextField dateField;
    private JTextField driverField;
    private JTextField reasonField;
    private JTextField kilometersField;
    private JList<String> tripsList;
    private DefaultListModel<String> tripsModel;
    private JLabel selectedVehicleLabel;
    
    private Map<String, DefaultListModel<String>> vehicleTrips = new HashMap<>();

    public static final String[] CAR_BRANDS = {
        "Audi",
        "BMW",
        "Ford",
        "Mercedes-Benz",
        "Opel",
        "Porsche",
        "Volkswagen",
        "Volvo",
        "Other"
    };
    
    private static final String OTHER_BRAND = "Other";

    private JButton editButton;
    private JButton deleteButton;
    
    // Patterns for different license plate parts
    private static final String LOCATION_PATTERN = "[A-ZÄÖÜ]{1,3}";  // Unterscheidungszeichen
    private static final String LETTERS_PATTERN = "[A-Z]{1,2}";      // Erkennungsnummer (Buchstaben)
    private static final String NUMBERS_PATTERN = "[1-9][0-9]{0,3}"; // Erkennungsnummer (Ziffern)
    
    private static class PlateDocument extends javax.swing.text.PlainDocument {
        private boolean isValidChar(char c, int position, String currentText) {
            // First 1-3 chars must be letters (location)
            if (position <= 2) {
                return Character.isLetter(c);
            }
            // Next 1-2 chars must be letters (after the dash)
            else if (position <= 4) {
                return Character.isLetter(c);
            }
            // Next 1-4 chars must be numbers (after the space)
            else if (position <= 8) {
                return Character.isDigit(c);
            }
            // Last position can be H or E
            else if (position == 9) {
                return c == 'H' || c == 'E';
            }
            return false;
        }

        @Override
        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                throws javax.swing.text.BadLocationException {
            if (str == null) return;
            
            // Convert to uppercase
            str = str.toUpperCase();
            
            // Get current content
            String currentText = getText(0, getLength());
            String newText = new StringBuilder(currentText).insert(offs, str).toString();
            
            // Remove formatting for length check
            String cleaned = newText.replace("-", "").replace(" ", "");
            
            // Check maximum length
            if (cleaned.length() > 9) return; // Max 8 chars + possible H/E
            
            // Build formatted text
            StringBuilder formatted = new StringBuilder();
            int pos = 0;
            
            for (char c : cleaned.toCharArray()) {
                // Validate character at this position
                if (!isValidChar(c, pos, cleaned)) {
                    return;
                }
                
                // Add formatting
                if (pos == 0) {
                    formatted.append(c);
                } else if (pos <= 2) {
                    formatted.append(c);
                } else if (pos == 3) {
                    formatted.append('-').append(c);
                } else if (pos == 4) {
                    formatted.append(c);
                } else if (pos == 5) {
                    formatted.append(' ').append(c);
                } else {
                    formatted.append(c);
                }
                pos++;
            }
            
            // Update document
            super.remove(0, getLength());
            super.insertString(0, formatted.toString(), a);
        }
    }
    
    public static boolean isValidLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.isEmpty()) {
            return false;
        }
        
        // Clean the input
        String cleaned = licensePlate.toUpperCase().trim();
        
        // Check basic format
        if (!cleaned.contains("-") || !cleaned.contains(" ")) {
            return false;
        }
        
        // Split into parts
        String[] mainParts = cleaned.split("-");
        if (mainParts.length != 2) return false;
        
        String location = mainParts[0];
        String[] numberParts = mainParts[1].split(" ");
        if (numberParts.length != 2) return false;
        
        String letters = numberParts[0];
        String numbers = numberParts[1];
        
        // Handle H/E suffix
        String numbersPart = numbers;
        if (numbers.endsWith("H") || numbers.endsWith("E")) {
            numbersPart = numbers.substring(0, numbers.length() - 1);
        }
        
        // Validate each part
        return location.matches(LOCATION_PATTERN) &&
               letters.matches(LETTERS_PATTERN) &&
               numbersPart.matches(NUMBERS_PATTERN) &&
               cleaned.length() <= 10; // Including separators
    }
    
    private void showLicensePlateHelp() {
        JOptionPane.showMessageDialog(this,
            """
            Bitte geben Sie ein gültiges Kennzeichen ein.
            
            Format: XXX-YY 1234Z
            
            • Unterscheidungszeichen (1-3 Buchstaben)
            • Bindestrich (-)
            • 1-2 Buchstaben
            • Leerzeichen
            • 1-4 Ziffern (keine führende 0)
            • Optional: H (Historisch) oder E (Elektro)
            
            Beispiele:
            • B-AB 123
            • M-XY 4567
            • HH-AB 42
            • B-AB 123H
            • M-XX 55E
            
            Maximale Länge: 8 Zeichen (ohne Bindestrich/Leerzeichen)
            """,
            "Kennzeichen Format",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public FuhrparkUI() {
        setTitle("Fuhrpark Verwaltung");
        setLayout(new BorderLayout(10, 10));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        JPanel leftPanel = createVehiclePanel();
        splitPane.setLeftComponent(leftPanel);
        
        logbookPanel = createLogbookPanel();
        logbookPanel.setVisible(false);
        splitPane.setRightComponent(logbookPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        vehicleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedVehicle = vehicleList.getSelectedValue();
                updateLogbookPanel(selectedVehicle);
            }
        });
        
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    
    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        inputPanel.add(new JLabel("Fahrzeugtyp:"));
        vehicleTypeCombo = new JComboBox<>(new String[]{"PKW", "LKW"});
        inputPanel.add(vehicleTypeCombo);
        
        inputPanel.add(new JLabel("Marke:"));
        brandCombo = new JComboBox<>(CAR_BRANDS);
        inputPanel.add(brandCombo);
        
        inputPanel.add(new JLabel("Modell:"));
        modelField = new JTextField();
        inputPanel.add(modelField);
        
        inputPanel.add(new JLabel("Kennzeichen:"));
        licensePlateField = createLicensePlateField();
        inputPanel.add(licensePlateField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Fahrzeug hinzufügen");
        editButton = new JButton("Bearbeiten");
        deleteButton = new JButton("Löschen");
        
        saveButton.addActionListener(e -> addVehicle());
        editButton.addActionListener(e -> editVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        
        inputPanel.add(new JLabel(""));
        inputPanel.add(buttonPanel);
        
        listModel = new DefaultListModel<>();
        vehicleList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Fahrzeuge"));
        
        vehicleList.addListSelectionListener(e -> {
            boolean hasSelection = vehicleList.getSelectedIndex() != -1;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        return panel;
    }
    
    private JPanel createLogbookPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Fahrtenbuch"));
        
        selectedVehicleLabel = new JLabel("Kein Fahrzeug ausgewählt");
        selectedVehicleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(selectedVehicleLabel, BorderLayout.NORTH);
        
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Datum:"));
        dateField = new JTextField(LocalDate.now().toString());
        inputPanel.add(dateField);
        
        inputPanel.add(new JLabel("Fahrer/Firma:"));
        driverField = new JTextField();
        inputPanel.add(driverField);
        
        inputPanel.add(new JLabel("Grund:"));
        reasonField = new JTextField();
        inputPanel.add(reasonField);
        
        inputPanel.add(new JLabel("Kilometer:"));
        kilometersField = new JTextField();
        inputPanel.add(kilometersField);
        
        JButton addTripButton = new JButton("Fahrt hinzufügen");
        addTripButton.addActionListener(e -> addTrip());
        inputPanel.add(new JLabel(""));
        inputPanel.add(addTripButton);
        
        tripsModel = new DefaultListModel<>();
        tripsList = new JList<>(tripsModel);
        JScrollPane scrollPane = new JScrollPane(tripsList);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateLogbookPanel(String selectedVehicle) {
        if (selectedVehicle == null) {
            logbookPanel.setVisible(false);
            return;
        }
        
        selectedVehicleLabel.setText("Fahrtenbuch für: " + selectedVehicle);
        
        tripsModel = vehicleTrips.computeIfAbsent(selectedVehicle, k -> new DefaultListModel<>());
        tripsList.setModel(tripsModel);
        
        logbookPanel.setVisible(true);
        clearLogbookInputs();
    }
    
    private void addTrip() {
        String selectedVehicle = vehicleList.getSelectedValue();
        if (selectedVehicle == null) {
            JOptionPane.showMessageDialog(this, 
                "Bitte wählen Sie zuerst ein Fahrzeug aus.", 
                "Fehler", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String date = dateField.getText().trim();
        String driver = driverField.getText().trim();
        String reason = reasonField.getText().trim();
        String kilometers = kilometersField.getText().trim();
        
        if (driver.isEmpty() || reason.isEmpty() || kilometers.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Bitte alle Felder ausfüllen", 
                "Fehler", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String tripEntry = String.format("%s: %s - %s (%s km)", 
            date, driver, reason, kilometers);
        tripsModel.addElement(tripEntry);
        
        clearLogbookInputs();
    }
    
    private void clearLogbookInputs() {
        dateField.setText(LocalDate.now().toString());
        driverField.setText("");
        reasonField.setText("");
        kilometersField.setText("");
    }
    
    public boolean isLicensePlateInUse(String licensePlate, int excludeIndex) {
        for (int i = 0; i < listModel.getSize(); i++) {
            if (i == excludeIndex) continue; // Skip the current vehicle when editing
            
            String vehicle = listModel.getElementAt(i);
            String existingPlate = vehicle.split("\\[|\\]")[1].trim();
            if (existingPlate.equalsIgnoreCase(licensePlate)) {
                return true;
            }
        }
        return false;
    }

    private void addVehicle() {
        String type = (String) vehicleTypeCombo.getSelectedItem();
        String selectedBrand = (String) brandCombo.getSelectedItem();
        String brand = OTHER_BRAND.equals(selectedBrand) ? customBrandField.getText().trim() : selectedBrand;
        String model = modelField.getText().trim();
        String licensePlate = licensePlateField.getText().trim().toUpperCase();
        
        if (brand.isEmpty() || model.isEmpty() || licensePlate.isEmpty() || 
            (OTHER_BRAND.equals(selectedBrand) && customBrandField.getText().trim().isEmpty())) {
            JOptionPane.showMessageDialog(this, 
                "Bitte alle Felder ausfüllen", 
                "Fehler", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isValidLicensePlate(licensePlate)) {
            showLicensePlateHelp();
            licensePlateField.requestFocus();
            return;
        }

        // Check for duplicate license plate
        if (isLicensePlateInUse(licensePlate, -1)) {
            JOptionPane.showMessageDialog(this,
                "Ein Fahrzeug mit diesem Kennzeichen existiert bereits.",
                "Duplikat Kennzeichen",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String vehicleEntry = String.format("%s [%s] %s %s", 
            type, licensePlate, brand, model);
        listModel.addElement(vehicleEntry);
        
        clearInputFields();
    }
    
    private void editVehicle() {
        int selectedIndex = vehicleList.getSelectedIndex();
        if (selectedIndex == -1) return;
        
        String vehicleEntry = listModel.getElementAt(selectedIndex);
        
        String[] parts = vehicleEntry.split("\\[|\\]");
        String type = parts[0].trim();
        String licensePlate = parts[1].trim();
        String[] brandModel = parts[2].trim().split(" ", 2);
        String brand = brandModel[0];
        String model = brandModel[1];
        
        VehicleEditDialog dialog = new VehicleEditDialog(
            this, type, brand, model, licensePlate, selectedIndex);
        dialog.setVisible(true);
        
        if (dialog.isSaveClicked()) {
            String newVehicleEntry = String.format("%s [%s] %s %s",
                dialog.getVehicleType(),
                dialog.getLicensePlate(),
                dialog.getBrand(),
                dialog.getModel());
                
            listModel.setElementAt(newVehicleEntry, selectedIndex);
        }
    }
    
    private void deleteVehicle() {
        int selectedIndex = vehicleList.getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Möchten Sie das ausgewählte Fahrzeug wirklich löschen?",
                "Fahrzeug löschen",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                listModel.remove(selectedIndex);
                clearInputFields();
            }
        }
    }
    
    private void clearInputFields() {
        brandCombo.setSelectedIndex(0);
        customBrandField.setText("");
        customBrandField.setVisible(false);
        modelField.setText("");
        licensePlateField.setText("");
        saveButton.setText("Fahrzeug hinzufügen");
    }
    
    public JTextField createLicensePlateField() {
        JTextField field = new JTextField();
        field.setDocument(new PlateDocument());
        return field;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FuhrparkUI().setVisible(true);
        });
    }
} 
