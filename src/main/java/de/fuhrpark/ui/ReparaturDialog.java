package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReparaturDialog extends JDialog {
    private ReparaturBuchEintrag result = null;
    private final JTextField beschreibungField;
    private final JTextField kostenField;
    private final JTextField werkstattField;
    private final JSpinner datumSpinner;
    private final String kennzeichen;

    public ReparaturDialog(Frame owner, String kennzeichen) {
        super(owner, "Neue Reparatur", true);
        this.kennzeichen = kennzeichen;
        
        // Initialize components
        this.beschreibungField = new JTextField(30);
        this.kostenField = new JTextField(10);
        this.werkstattField = new JTextField(30);
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        this.datumSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datumSpinner, "dd.MM.yyyy");
        datumSpinner.setEditor(dateEditor);
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Beschreibung:"));
        inputPanel.add(beschreibungField);
        inputPanel.add(new JLabel("Kosten (€):"));
        inputPanel.add(kostenField);
        inputPanel.add(new JLabel("Werkstatt:"));
        inputPanel.add(werkstattField);
        inputPanel.add(new JLabel("Datum:"));
        inputPanel.add(datumSpinner);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        JButton cancelButton = new JButton("Abbrechen");

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                result = createReparaturBuchEintrag();
                dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            result = null;
            dispose();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Dialog properties
        setResizable(false);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private ReparaturBuchEintrag createReparaturBuchEintrag() {
        try {
            double kosten = Double.parseDouble(kostenField.getText().trim().replace(",", "."));
            java.util.Date date = (java.util.Date) datumSpinner.getValue();
            LocalDate datum = LocalDate.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
            
            return new ReparaturBuchEintrag(
                kennzeichen,
                kosten,
                beschreibungField.getText().trim(),
                werkstattField.getText().trim(),
                datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            );
        } catch (NumberFormatException ex) {
            showError("Bitte geben Sie einen gültigen Kostenbetrag ein.");
            return null;
        }
    }

    private boolean validateInput() {
        if (beschreibungField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Beschreibung ein.");
            return false;
        }
        if (werkstattField.getText().trim().isEmpty()) {
            showError("Bitte geben Sie eine Werkstatt ein.");
            return false;
        }
        try {
            Double.parseDouble(kostenField.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            showError("Bitte geben Sie einen gültigen Kostenbetrag ein.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Validierungsfehler",
            JOptionPane.ERROR_MESSAGE);
    }

    public ReparaturBuchEintrag getResult() {
        return result;
    }
} 