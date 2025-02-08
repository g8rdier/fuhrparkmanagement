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

    private static final String[] CAR_BRANDS = {
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
        licensePlateField = new JTextField();
        inputPanel.add(licensePlateField);
        
        saveButton = new JButton("Fahrzeug hinzufügen");
        saveButton.addActionListener(e -> addVehicle());
        inputPanel.add(new JLabel(""));
        inputPanel.add(saveButton);
        
        listModel = new DefaultListModel<>();
        vehicleList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(vehicleList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Fahrzeuge"));
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
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
    
    private void addVehicle() {
        String type = (String) vehicleTypeCombo.getSelectedItem();
        String selectedBrand = (String) brandCombo.getSelectedItem();
        String brand = OTHER_BRAND.equals(selectedBrand) ? customBrandField.getText().trim() : selectedBrand;
        String model = modelField.getText().trim();
        String licensePlate = licensePlateField.getText().trim();
        
        if (brand.isEmpty() || model.isEmpty() || licensePlate.isEmpty() || 
            (OTHER_BRAND.equals(selectedBrand) && customBrandField.getText().trim().isEmpty())) {
            JOptionPane.showMessageDialog(this, 
                "Bitte alle Felder ausfüllen", 
                "Fehler", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String vehicleEntry = String.format("%s [%s] %s %s", 
            type, licensePlate, brand, model);
        listModel.addElement(vehicleEntry);
        
        brandCombo.setSelectedIndex(0);
        customBrandField.setText("");
        customBrandField.setVisible(false);
        modelField.setText("");
        licensePlateField.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FuhrparkUI().setVisible(true);
        });
    }
} 
