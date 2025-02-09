package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.ui.model.FahrzeugTableModel;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.text.MaskFormatter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings({"unused", "all"})  // This will suppress all warnings for this class
public class FahrzeugDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final JComboBox<String> typComboBox;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JFormattedTextField kennzeichenField;
    private final JFormattedTextField wertField;
    private final FahrzeugTableModel tableModel;
    private boolean confirmed = false;
    private final boolean isEditMode;

    // Constructor for new vehicles
    public FahrzeugDialog(JFrame owner, FahrzeugTableModel tableModel) {
        super(owner, "Neues Fahrzeug", true);
        this.tableModel = tableModel;
        this.isEditMode = false;
        
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = createKennzeichenField();
        this.wertField = createWertField();
        
        initComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                setVisible(false);
            }
        });
    }

    // Constructor for editing existing vehicles
    public FahrzeugDialog(JFrame owner, FahrzeugTableModel tableModel, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        this.tableModel = tableModel;
        this.isEditMode = true;
        
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.typComboBox.setSelectedItem(fahrzeug.getTyp());
        this.typComboBox.setEnabled(false);
        
        this.markeField = new JTextField(fahrzeug.getMarke(), 20);
        this.markeField.setEnabled(false);
        
        this.modellField = new JTextField(fahrzeug.getModell(), 20);
        this.modellField.setEnabled(false);
        
        this.kennzeichenField = createKennzeichenField();
        this.kennzeichenField.setText(fahrzeug.getKennzeichen());
        
        this.wertField = createWertField();
        this.wertField.setValue(fahrzeug.getPreis());
        
        initComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                setVisible(false);
            }
        });
    }

    private JFormattedTextField createKennzeichenField() {
        try {
            // Format: XXX-XX1234 (where X can be A-Z and 1234 can be 0-9)
            MaskFormatter formatter = new MaskFormatter("UUU-UU####");
            formatter.setPlaceholderCharacter('_');
            formatter.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
            
            final JFormattedTextField field = new JFormattedTextField(formatter);
            field.setColumns(10);
            
            // Add focus listener for validation
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    validateKennzeichen(field);
                }
            });
            
            return field;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private boolean validateKennzeichen(JFormattedTextField field) {
        String value = field.getText().replace("_", "").trim();
        
        // Basic format validation
        if (value.length() < 5) { // At least: XX-X1
            return false;
        }

        // Check if contains hyphen at correct position
        int hyphenPos = value.indexOf('-');
        if (hyphenPos == -1 || hyphenPos > 3) {
            return false;
        }

        // Split parts
        String[] parts = value.split("-");
        if (parts.length != 2) {
            return false;
        }

        // Validate first part (1-3 letters)
        String firstPart = parts[0];
        if (firstPart.length() < 1 || firstPart.length() > 3 || !firstPart.matches("[A-Z]+")) {
            return false;
        }

        // Validate second part (letters followed by numbers)
        String secondPart = parts[1];
        if (!secondPart.matches("[A-Z]+[0-9]+")) {
            return false;
        }

        return true;
    }

    private JFormattedTextField createWertField() {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setGroupingUsed(true);

        JFormattedTextField field = new JFormattedTextField(format);
        field.setValue(0.0);
        field.setColumns(10);

        // Add input verification
        field.addPropertyChangeListener("value", evt -> {
            Object value = field.getValue();
            if (value instanceof Number) {
                double numValue = ((Number) value).doubleValue();
                if (numValue > 10000000) {  // 10 million limit
                    field.setValue(10000000.0);
                }
            }
        });

        return field;
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Typ
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Typ:"), gbc);
        gbc.gridx = 1;
        add(typComboBox, gbc);

        // Marke
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Marke:"), gbc);
        gbc.gridx = 1;
        add(markeField, gbc);

        // Modell
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Modell:"), gbc);
        gbc.gridx = 1;
        add(modellField, gbc);

        // Kennzeichen
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Kennzeichen:"), gbc);
        gbc.gridx = 1;
        add(kennzeichenField, gbc);

        // Aktueller Wert
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Aktueller Wert:"), gbc);
        gbc.gridx = 1;
        add(wertField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        okButton.addActionListener(event -> okButtonClicked());
        cancelButton.addActionListener(event -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(getOwner());
    }

    public boolean showDialog() {
        confirmed = false;
        setVisible(true);
        return confirmed;
    }

    public String getSelectedType() {
        return (String) typComboBox.getSelectedItem();
    }

    public String getMarke() {
        return markeField.getText().trim();
    }

    public String getModell() {
        return modellField.getText().trim();
    }

    public String getKennzeichen() {
        String raw = kennzeichenField.getText();
        // Keep partial input, just remove underscores
        return raw.replace("_", "").trim();
    }

    public double getPreis() {
        Object value = wertField.getValue();
        return (value instanceof Double) ? (Double) value : 0.0;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private boolean isKennzeichenDuplicate(String kennzeichen) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingKennzeichen = (String) tableModel.getValueAt(i, 1); // Column 1 is Kennzeichen
            if (kennzeichen.equals(existingKennzeichen)) {
                return true;
            }
        }
        return false;
    }

    private void okButtonClicked() {
        if (!validateKennzeichen(kennzeichenField)) {
            JOptionPane.showMessageDialog(this,
                "Bitte geben Sie ein gültiges Kennzeichen ein.",
                "Ungültiges Kennzeichen",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String kennzeichen = getKennzeichen();
        if (!isEditMode && isKennzeichenDuplicate(kennzeichen)) {
            JOptionPane.showMessageDialog(this,
                "Dieses Kennzeichen existiert bereits.",
                "Duplikat gefunden",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }
} 