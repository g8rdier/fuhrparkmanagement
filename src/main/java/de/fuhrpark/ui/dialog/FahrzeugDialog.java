package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class FahrzeugDialog extends JDialog {
    private final JComboBox<String> typComboBox;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JTextField kennzeichenField;
    private final JFormattedTextField preisField;
    private final JLabel aktuellerWertLabel;
    private boolean confirmed = false;

    // Constructor for new vehicle
    public FahrzeugDialog(JFrame owner) {
        super(owner, "Neues Fahrzeug", true);
        
        // Initialize components
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.kennzeichenField = createKennzeichenField();
        this.preisField = createPreisField();
        this.aktuellerWertLabel = new JLabel("0,00 €");
        
        initComponents();
    }

    // Constructor for editing existing vehicle
    public FahrzeugDialog(JFrame owner, Fahrzeug fahrzeug) {
        super(owner, "Fahrzeug bearbeiten", true);
        this.typComboBox = new JComboBox<>(new String[]{"PKW", "LKW"});
        this.markeField = new JTextField(fahrzeug.getMarke(), 20);
        this.modellField = new JTextField(fahrzeug.getModell(), 20);
        this.kennzeichenField = createKennzeichenField();
        this.kennzeichenField.setText(fahrzeug.getKennzeichen());
        this.preisField = createPreisField();
        this.preisField.setValue(fahrzeug.getPreis());
        this.aktuellerWertLabel = new JLabel(String.format(Locale.GERMANY, "%.2f €", fahrzeug.berechneAktuellenWert()));
        
        // Disable type selection and license plate for existing vehicles
        this.typComboBox.setSelectedItem(fahrzeug.getTyp());
        this.typComboBox.setEnabled(false);
        this.kennzeichenField.setEnabled(false);
        
        initComponents();
        setupWertCalculation();
    }

    private JTextField createKennzeichenField() {
        JTextField field = new JTextField(10);
        field.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str == null) return;

                String text = getText(0, getLength());
                String newText = text.substring(0, offset) + str + text.substring(offset);
                
                if (isValidKennzeichenInput(newText)) {
                    // Format while typing
                    if (offset == 3 && !newText.contains("-")) {
                        super.insertString(offset, "-", attr);
                        offset++;
                    }
                    super.insertString(offset, str.toUpperCase(), attr);
                }
            }

            private boolean isValidKennzeichenInput(String input) {
                // Remove the hyphen for validation
                String normalized = input.replace("-", "");
                
                // Max length check (3 letters + 2 letters + 4 numbers = 9, plus 1 for hyphen = 10)
                if (normalized.length() > 9) return false;

                // Split into parts
                String firstPart = normalized.substring(0, Math.min(3, normalized.length()));
                String secondPart = normalized.length() > 3 ? 
                    normalized.substring(3, Math.min(5, normalized.length())) : "";
                String numberPart = normalized.length() > 5 ? normalized.substring(5) : "";

                // Validate first part (1-3 letters)
                if (!firstPart.isEmpty() && !firstPart.matches("[A-Z]{1,3}")) return false;

                // Validate second part (1-2 letters)
                if (!secondPart.isEmpty() && !secondPart.matches("[A-Z]{1,2}")) return false;

                // Validate number part (1-4 numbers)
                if (!numberPart.isEmpty() && !numberPart.matches("[0-9]{1,4}")) return false;

                return true;
            }
        });

        return field;
    }

    private JFormattedTextField createPreisField() {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMANY);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        JFormattedTextField field = new JFormattedTextField(formatter);
        field.setValue(0.0);
        return field;
    }

    private void setupWertCalculation() {
        preisField.addPropertyChangeListener("value", evt -> {
            try {
                double preis = getPreis();
                double wert = "PKW".equals(getSelectedType()) ? preis * 0.9 : preis * 0.85;
                aktuellerWertLabel.setText(String.format(Locale.GERMANY, "%.2f €", wert));
            } catch (Exception e) {
                aktuellerWertLabel.setText("0,00 €");
            }
        });

        typComboBox.addActionListener(e -> {
            try {
                double preis = getPreis();
                double wert = "PKW".equals(getSelectedType()) ? preis * 0.9 : preis * 0.85;
                aktuellerWertLabel.setText(String.format(Locale.GERMANY, "%.2f €", wert));
            } catch (Exception ex) {
                aktuellerWertLabel.setText("0,00 €");
            }
        });
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
        add(aktuellerWertLabel, gbc);

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
            Number preis = (Number) preisField.getValue();
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
        return kennzeichenField.getText().replace("_", "").trim();
    }

    public double getPreis() {
        try {
            Number value = (Number) preisField.getValue();
            return value != null ? value.doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
} 