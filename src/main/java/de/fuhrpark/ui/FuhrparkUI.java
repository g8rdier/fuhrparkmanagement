package de.fuhrpark.ui;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.text.ParseException;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.ui.dialog.VehicleEditDialog;
import de.fuhrpark.ui.model.VehicleData;
import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.dialog.FahrtenbuchDialog;
import de.fuhrpark.ui.model.FahrzeugData;
import de.fuhrpark.ui.model.PreisDocument;
import de.fuhrpark.ui.model.FahrzeugTableModel;
import java.util.List;

public class FuhrparkUI extends JFrame {
    // Constants
    private static final String[] VEHICLE_TYPES = {"PKW", "LKW"};
    private static final String LOCATION_PATTERN = "[A-ZÄÖÜ]{1,3}";
    private static final String LETTERS_PATTERN = "[A-Z]{1,2}";
    private static final String NUMBERS_PATTERN = "[1-9][0-9]{0,3}";
    private static final double MINIMUM_VEHICLE_PRICE = 500.0; // Minimum 500 euros for any vehicle
    
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
    
    private final FuhrparkManager manager;
    private final JTable fahrzeugTable;
    
    private final FahrzeugService fahrzeugService;
    private final FahrzeugFactory fahrzeugFactory;
    private final FahrzeugTableModel tableModel;

    public FuhrparkUI(FuhrparkManager manager, FahrzeugService service, FahrzeugFactory factory) {
        super("Fuhrpark Manager");
        if (manager == null || service == null || factory == null) {
            throw new IllegalArgumentException("Manager und Service und Factory dürfen nicht null sein");
        }
        this.manager = manager;
        this.fahrzeugService = service;
        this.fahrzeugFactory = factory;
        this.tableModel = new FahrzeugTableModel();
        this.fahrzeugTable = new JTable(tableModel);
        
        // Initialize all fields in constructor before calling initComponents
        fahrzeugTypComboBox = new JComboBox<>(VEHICLE_TYPES);
        markeField = new JTextField();
        modelField = new JTextField();
        licensePlateField = new JTextField();
        priceField = new JTextField();
        priceField.setDocument(new PriceDocument());
        listModel = new DefaultListModel<>();
        vehicleList = new JList<>(listModel);
        editButton = new JButton("Bearbeiten");
        deleteButton = new JButton("Löschen");
        addButton = new JButton("Fahrzeug hinzufügen");

        initComponents();
        loadFahrzeuge();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar toolbar = createToolbar();
        add(toolbar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        add(scrollPane, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JButton addButton = new JButton("Hinzufügen");
        JButton editButton = new JButton("Bearbeiten");
        JButton deleteButton = new JButton("Löschen");
        JButton fahrtenbuchButton = new JButton("Fahrtenbuch");

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteFahrzeug());
        fahrtenbuchButton.addActionListener(e -> showFahrtenbuch());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(fahrtenbuchButton);

        return toolbar;
    }
    
    private void loadFahrzeuge() {
        List<Fahrzeug> fahrzeuge = fahrzeugService.getFahrzeuge();
        tableModel.updateData(fahrzeuge);
    }
    
    private void showAddDialog() {
        // TODO: Implement after FahrzeugDialog is created
        JOptionPane.showMessageDialog(this, "Funktion noch nicht implementiert");
    }
    
    private void showEditDialog() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            // TODO: Implement after FahrzeugDialog is created
            JOptionPane.showMessageDialog(this, "Funktion noch nicht implementiert");
        }
    }
    
    private void deleteFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            Fahrzeug fahrzeug = tableModel.getFahrzeug(selectedRow);
            int option = JOptionPane.showConfirmDialog(this,
                "Möchten Sie das Fahrzeug wirklich löschen?",
                "Fahrzeug löschen",
                JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                fahrzeugService.loescheFahrzeug(fahrzeug.getKennzeichen());
                loadFahrzeuge();
            }
        }
    }
    
    private void showFahrtenbuch() {
        // TODO: Implement after FahrtenbuchDialog is created
        JOptionPane.showMessageDialog(this, "Funktion noch nicht implementiert");
    }
    
    private void addVehicle() {
        String type = (String) fahrzeugTypComboBox.getSelectedItem();
        String brand = markeField.getText().trim();
        String model = modelField.getText().trim();
        String licensePlate = licensePlateField.getText().trim();
        String priceText = priceField.getText().trim();

        // Validate required fields
        if (brand.isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return;
        }
        if (licensePlate.isEmpty()) {
            showError("Bitte geben Sie ein Kennzeichen ein.");
            return;
        }

        // Check for duplicate license plate
        if (isLicensePlateExists(licensePlate, -1)) {
            showError("Ein Fahrzeug mit diesem Kennzeichen existiert bereits.");
            return;
        }

        // Price validation
        try {
            NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMANY);
            Number number = format.parse(priceText);
            double priceValue = number.doubleValue();
            
            if (priceValue < MINIMUM_VEHICLE_PRICE) {
                showError("Der Kaufpreis muss mindestens " + 
                    NumberFormat.getCurrencyInstance(Locale.GERMANY).format(MINIMUM_VEHICLE_PRICE) + 
                    " betragen.");
                return;
            }

            String formattedPrice = NumberFormat.getCurrencyInstance(Locale.GERMANY)
                .format(priceValue);
            
            // Use empty string for optional fields if they're not filled
            String brandDisplay = brand.isEmpty() ? "-" : brand;
            String modelDisplay = model.isEmpty() ? "-" : model;
            
            String vehicleEntry = String.format("%s [%s] %s %s - %s",
                type, licensePlate, brandDisplay, modelDisplay, formattedPrice);
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
            showError("Bitte wählen Sie ein Fahrzeug aus.");
            return;
        }

        String entry = listModel.getElementAt(selectedIndex);
        VehicleEditDialog dialog = new VehicleEditDialog(this, parseVehicleEntry(entry));
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            // Check for duplicate license plate, excluding the current entry
            if (isLicensePlateExists(dialog.getLicensePlate(), selectedIndex)) {
                showError("Ein Fahrzeug mit diesem Kennzeichen existiert bereits.");
                return;
            }

            try {
                NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMANY);
                Number number = format.parse(dialog.getPrice());
                double priceValue = number.doubleValue();
                
                if (priceValue < MINIMUM_VEHICLE_PRICE) {
                    showError("Der Kaufpreis muss mindestens " + 
                        NumberFormat.getCurrencyInstance(Locale.GERMANY).format(MINIMUM_VEHICLE_PRICE) + 
                        " betragen.");
                    return;
                }

                String formattedPrice = NumberFormat.getCurrencyInstance(Locale.GERMANY)
                    .format(priceValue);
                
                String modelDisplay = dialog.getModel().trim().isEmpty() ? "-" : dialog.getModel();
                
                String newEntry = String.format("%s [%s] %s %s - %s",
                    dialog.getVehicleType(), dialog.getLicensePlate(), 
                    dialog.getBrand(), modelDisplay, formattedPrice);
                    
                listModel.setElementAt(newEntry, selectedIndex);
            } catch (ParseException e) {
                showError("Bitte geben Sie einen gültigen Kaufpreis ein.");
            }
        }
    }
    
    private void deleteVehicle() {
        int selectedIndex = vehicleList.getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Möchten Sie das ausgewählte Fahrzeug wirklich löschen?",
                "Fahrzeug löschen",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                listModel.remove(selectedIndex);
            }
        }
    }
    
    private void clearFields() {
        fahrzeugTypComboBox.setSelectedIndex(0);
        markeField.setText("");
        modelField.setText("");
        licensePlateField.setText("");
        priceField.setText("");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Fehler",
            JOptionPane.ERROR_MESSAGE
        );
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
    
    // Helper method to parse vehicle entry for editing
    private VehicleData parseVehicleEntry(String entry) {
        VehicleData data = new VehicleData();
        
        // Extract type (everything before the first '[')
        int typeEnd = entry.indexOf('[');
        data.setType(entry.substring(0, typeEnd).trim());
        
        // Extract license plate (between '[' and ']')
        int plateStart = typeEnd + 1;
        int plateEnd = entry.indexOf(']');
        data.setLicensePlate(entry.substring(plateStart, plateEnd).trim());
        
        // Extract remaining parts
        String[] parts = entry.substring(plateEnd + 1).split("-");
        
        // Extract brand and model, handling the case where model is "-"
        String brandModelPart = parts[0].trim();
        String[] brandModel = brandModelPart.split(" ", 2);
        data.setBrand(brandModel[0].trim());
        
        // Handle model: if it's "-" or not present, set as empty string
        if (brandModel.length > 1) {
            String model = brandModel[1].trim();
            data.setModel(model.equals("-") ? "" : model);
        } else {
            data.setModel("");
        }
        
        // Extract price (last part)
        data.setPrice(parts[parts.length - 1].trim());
        
        return data;
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

    private boolean isLicensePlateExists(String licensePlate, int excludeIndex) {
        for (int i = 0; i < listModel.getSize(); i++) {
            if (i == excludeIndex) continue; // Skip current item when editing
            
            String entry = listModel.getElementAt(i);
            String existingPlate = extractLicensePlate(entry);
            if (existingPlate.equalsIgnoreCase(licensePlate)) {
                return true;
            }
        }
        return false;
    }

    private String extractLicensePlate(String entry) {
        int start = entry.indexOf('[') + 1;
        int end = entry.indexOf(']');
        return entry.substring(start, end).trim();
    }
} 
