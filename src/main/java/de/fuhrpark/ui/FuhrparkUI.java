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
            int charCount = 0;
            
            for (char c : cleaned.toCharArray()) {
                // Validate character based on position
                if (charCount <= 4) {
                    // First 5 characters must be letters
                    if (!Character.isLetter(c)) return;
                } else if (charCount <= 7) {
                    // Next 3 characters must be numbers
                    if (!Character.isDigit(c)) return;
                } else {
                    // Last character can be H or E
                    if (c != 'H' && c != 'E') return;
                }
                
                // Add the character
                formatted.append(c);
                charCount++;
                
                // Add separators at the right positions
                if (charCount == 3 && charCount < cleaned.length()) {
                    formatted.append('-');
                } else if (charCount == 5 && charCount < cleaned.length()) {
                    formatted.append(' ');
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
            """,
            "Kennzeichen Format",
            JOptionPane.INFORMATION_MESSAGE);
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
        // TODO: Implement edit functionality
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FuhrparkUI().setVisible(true);
        });
    }
} 
