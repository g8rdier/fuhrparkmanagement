package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.enums.ReparaturTyp;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ReparaturDialog extends JDialog {
    private final JTextField kennzeichenField = new JTextField(20);
    private final JTextField werkstattField = new JTextField(20);
    private final JTextField kostenField = new JTextField(20);
    private final JTextField beschreibungField = new JTextField(20);
    private final JComboBox<ReparaturTyp> typComboBox = new JComboBox<>(ReparaturTyp.values());
    private ReparaturBuchEintrag result = null;

    public ReparaturDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        
        // Layout setup
        setLayout(new BorderLayout());
        
        // Create main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add components
        addComponent(mainPanel, new JLabel("Kennzeichen:"), kennzeichenField, gbc, 0);
        addComponent(mainPanel, new JLabel("Werkstatt:"), werkstattField, gbc, 1);
        addComponent(mainPanel, new JLabel("Kosten:"), kostenField, gbc, 2);
        addComponent(mainPanel, new JLabel("Beschreibung:"), beschreibungField, gbc, 3);
        addComponent(mainPanel, new JLabel("Typ:"), typComboBox, gbc, 4);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        okButton.addActionListener(_ -> {
            if (isInputValid()) {
                result = createReparaturBuchEintrag();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Bitte füllen Sie alle Felder aus.", 
                    "Ungültige Eingabe", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(_ -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Dialog settings
        pack();
        setLocationRelativeTo(owner);
    }

    private void addComponent(JPanel panel, JLabel label, JComponent field, 
                            GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private boolean isInputValid() {
        return !kennzeichenField.getText().trim().isEmpty() &&
               !werkstattField.getText().trim().isEmpty() &&
               !kostenField.getText().trim().isEmpty() &&
               typComboBox.getSelectedItem() != null;
    }

    private ReparaturBuchEintrag createReparaturBuchEintrag() {
        try {
            return new ReparaturBuchEintrag(
                LocalDate.now(),
                (ReparaturTyp) typComboBox.getSelectedItem(),
                beschreibungField.getText().trim(),
                Double.parseDouble(kostenField.getText().trim()),
                kennzeichenField.getText().trim(),
                werkstattField.getText().trim()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Bitte geben Sie einen gültigen Kostenbetrag ein.",
                "Ungültige Eingabe",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public ReparaturBuchEintrag getResult() {
        return result;
    }
} 