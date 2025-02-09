package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.text.MaskFormatter;

public class FahrzeugDialog extends JDialog {
    private final JComboBox<String> typComboBox;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JFormattedTextField kennzeichenField;
    private final JFormattedTextField wertField;
    private boolean confirmed = false;

    // Constructor for new vehicles
    public FahrzeugDialog(JFrame owner) {
        super(owner, "Neues Fahrzeug", true);
        
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = createKennzeichenField();
        this.wertField = createWertField();
        
        initComponents();
        setupListeners();
    }

    // Constructor for editing existing vehicles
    public FahrzeugDialog(JFrame owner, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.typComboBox.setSelectedItem(fahrzeug.getTyp());
        this.typComboBox.setEnabled(false);  // Type can't be changed for existing vehicles
        
        this.markeField = new JTextField(fahrzeug.getMarke(), 20);
        this.modellField = new JTextField(fahrzeug.getModell(), 20);
        
        this.kennzeichenField = createKennzeichenField();
        this.kennzeichenField.setText(fahrzeug.getKennzeichen());
        this.kennzeichenField.setEnabled(false);  // License plate can't be changed
        
        this.wertField = createWertField();
        this.wertField.setValue(fahrzeug.getPreis());
        
        initComponents();
    }

    private JFormattedTextField createKennzeichenField() {
        try {
            // Format: XXX-XX9999 where X is letter and 9 is number
            MaskFormatter formatter = new MaskFormatter("UUU-UU####");
            formatter.setPlaceholderCharacter('_');
            
            // Allow both letters and numbers, but validation will ensure correct placement
            formatter.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
            
            final JFormattedTextField field = new JFormattedTextField(formatter);
            field.setColumns(10);
            
            return field;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private JFormattedTextField createWertField() {
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.GERMANY);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setGroupingUsed(true);
        
        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text.isEmpty()) return 0.0;
                text = text.replace("€", "").trim();
                return super.stringToValue(text);
            }
            
            @Override
            public String valueToString(Object value) throws ParseException {
                String str = super.valueToString(value);
                if (!str.isEmpty()) {
                    return str + " €";
                }
                return "0,00 €";
            }
        };
        
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setAllowsInvalid(false);
        
        JFormattedTextField field = new JFormattedTextField(formatter);
        field.setValue(0.0);
        field.setColumns(12);
        
        return field;
    }

    private void setupListeners() {
        // Add validation listener for kennzeichen
        kennzeichenField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateKennzeichen();
            }
        });

        // Add listener for typ to update aktueller wert
        typComboBox.addActionListener(e -> updateAktuellerWert());
    }

    private void validateKennzeichen() {
        String value = kennzeichenField.getText();
        String firstPart = value.substring(0, 3);
        String secondPart = value.substring(4, 6);
        String numberPart = value.substring(6);
        
        boolean isValid = !firstPart.replace("_", "").isEmpty() && 
                         !secondPart.replace("_", "").isEmpty() && 
                         !numberPart.replace("_", "").isEmpty();
        
        kennzeichenField.setBorder(isValid 
            ? UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border")
            : BorderFactory.createLineBorder(Color.RED));
    }

    private void updateAktuellerWert() {
        try {
            double preis = getPreis();
            String typ = (String) typComboBox.getSelectedItem();
            double faktor = "PKW".equals(typ) ? 0.9 : 0.85;
            double wert = preis * faktor;
            
            // Format with German locale (dots for thousands, comma for decimals)
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.GERMANY);
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            wertField.setText(df.format(wert) + " €");
        } catch (Exception e) {
            wertField.setText("0,00 €");
        }
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

        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(getOwner());
    }

    private boolean validateInputs() {
        if (markeField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return false;
        }
        if (modellField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie ein Modell ein.");
            return false;
        }
        String kennzeichen = getKennzeichen();
        if (kennzeichen.isEmpty() || kennzeichen.contains("_")) {
            showError("Bitte geben Sie ein vollständiges Kennzeichen ein.");
            return false;
        }
        try {
            Number preis = (Number) wertField.getValue();
            if (preis.doubleValue() <= 0) {
                showError("Der Preis muss größer als 0 sein.");
                return false;
            }
        } catch (Exception e) {
            showError("Bitte geben Sie einen gültigen Preis ein.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
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
        return raw.replace("_", "").trim();
    }

    public double getPreis() {
        Object value = wertField.getValue();
        return (value instanceof Double) ? (Double) value : 0.0;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
} 