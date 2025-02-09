package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.ui.model.FahrzeugTableModel;
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
    private final FahrzeugTableModel tableModel;
    private boolean confirmed = false;
    private final boolean isEditMode;
    private final Fahrzeug existingFahrzeug;

    // Constructor for new vehicles
    public FahrzeugDialog(JFrame owner, FahrzeugTableModel tableModel) {
        super(owner, "Neues Fahrzeug", true);
        this.tableModel = tableModel;
        this.isEditMode = false;
        this.existingFahrzeug = null;
        
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = createKennzeichenField();
        this.wertField = createWertField();
        
        initComponents();
    }

    // Constructor for editing existing vehicles
    public FahrzeugDialog(JFrame owner, FahrzeugTableModel tableModel, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        this.tableModel = tableModel;
        this.isEditMode = true;
        this.existingFahrzeug = fahrzeug;
        
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
    }

    private JFormattedTextField createKennzeichenField() {
        try {
            MaskFormatter formatter = new MaskFormatter("UUU-UU####");
            formatter.setPlaceholderCharacter('_');
            formatter.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
            
            final JFormattedTextField field = new JFormattedTextField(formatter);
            field.setColumns(10);
            
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
        String value = field.getText();
        
        // Split into the three parts
        String firstPart = value.substring(0, 3);        // First three letters
        String secondPart = value.substring(4, 6);       // Two letters after hyphen
        String numberPart = value.substring(6);          // Last four numbers
        
        // Count actual characters (excluding underscores)
        int firstPartLetters = firstPart.replace("_", "").length();
        int secondPartLetters = secondPart.replace("_", "").length();
        int numberPartDigits = numberPart.replace("_", "").length();
        
        // Validate minimum requirements
        boolean isValid = firstPartLetters >= 1 &&      // At least one letter in first part
                        secondPartLetters >= 1 &&        // At least one letter in second part
                        numberPartDigits >= 1;           // At least one number in last part
        
        // Visual feedback
        if (isValid) {
            field.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        } else {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        return isValid;
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

    private void setupListeners() {
        // Add validation listener for kennzeichen
        kennzeichenField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateKennzeichen(kennzeichenField);
            }
        });

        // Add listener for typ to update aktueller wert
        typComboBox.addActionListener(e -> updateAktuellerWert());
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

        okButton.addActionListener(e -> okButtonClicked());
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