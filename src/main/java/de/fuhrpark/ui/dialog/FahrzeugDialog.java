package de.fuhrpark.ui.dialog;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.ui.util.KennzeichenFormatter;
import de.fuhrpark.manager.FuhrparkManager;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.text.ParseException;

public class FahrzeugDialog extends JDialog {
    private final JComboBox<FahrzeugTyp> typComboBox;
    private final JFormattedTextField kennzeichenField;
    private final JTextField markeField;
    private final JTextField modellField;
    private final JTextField preisField;
    private boolean approved = false;
    private final FuhrparkManager manager;

    public FahrzeugDialog(Frame owner, FuhrparkManager manager) {
        super(owner, "Fahrzeug hinzufügen", true);
        this.manager = manager;
        
        // Initialize components
        this.typComboBox = new JComboBox<>(FahrzeugTyp.values());
        this.kennzeichenField = new JFormattedTextField(new KennzeichenFormatter());
        this.markeField = new JTextField(20);
        this.modellField = new JTextField(20);
        this.preisField = new JTextField(20);
        
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Typ:"), gbc);
        gbc.gridx = 1;
        add(typComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Kennzeichen:"), gbc);
        gbc.gridx = 1;
        add(kennzeichenField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Marke:"), gbc);
        gbc.gridx = 1;
        add(markeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Modell:"), gbc);
        gbc.gridx = 1;
        add(modellField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Preis:"), gbc);
        gbc.gridx = 1;
        add(preisField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");
        
        okButton.addActionListener(e -> onOK());
        
        cancelButton.addActionListener(e -> setVisible(false));
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(getOwner());
    }

    private void onOK() {
        if (validateInput()) {
            try {
                manager.addFahrzeug(
                    getSelectedTyp(),
                    getKennzeichen(),
                    getMarke(),
                    getModell(),
                    getPreis()
                );
                approved = true;
                setVisible(false);
            } catch (Exception e) {
                showError("Fehler beim Speichern: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        // First check if kennzeichen is valid
        String kennzeichen = kennzeichenField.getText();
        if (kennzeichen == null || !kennzeichen.matches("^[A-Z]{1,3}-[A-Z]{1,2}[1-9][0-9]{0,3}$")) {
            showError("Bitte geben Sie ein gültiges Kennzeichen ein (z.B. B-AB123)");
            kennzeichenField.setText("");  // Clear invalid input
            kennzeichenField.requestFocus();
            return false;
        }

        // Rest of validation
        if (getMarke().isEmpty()) {
            showError("Bitte geben Sie eine Marke ein.");
            return false;
        }
        if (getModell().isEmpty()) {
            showError("Bitte geben Sie ein Modell ein.");
            return false;
        }
        try {
            double preis = getPreis();
            if (preis <= 0) {
                showError("Der Preis muss größer als 0 sein.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Bitte geben Sie einen gültigen Preis ein.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Eingabefehler", JOptionPane.ERROR_MESSAGE);
    }

    public boolean showDialog() {
        approved = false;
        setVisible(true);
        return approved;
    }

    // Getter methods
    public FahrzeugTyp getSelectedTyp() {
        return (FahrzeugTyp) typComboBox.getSelectedItem();
    }

    public String getKennzeichen() {
        return kennzeichenField.getText().trim();
    }

    public String getMarke() {
        return markeField.getText().trim();
    }

    public String getModell() {
        return modellField.getText().trim();
    }

    public double getPreis() {
        return Double.parseDouble(preisField.getText().trim());
    }
} 