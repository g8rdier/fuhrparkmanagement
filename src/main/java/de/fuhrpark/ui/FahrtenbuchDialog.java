package de.fuhrpark.ui;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.service.FahrtenbuchService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FahrtenbuchDialog extends JDialog {
    private final JTable table;
    private final String kennzeichen;
    private final FahrtenbuchService service;
    private final JTextField startOrtField = new JTextField(20);
    private final JTextField zielOrtField = new JTextField(20);
    private final JTextField kilometerField = new JTextField(20);
    private final JTextField kennzeichenField = new JTextField(20);
    private final JTextField fahrerNameField = new JTextField(20);
    private final JTextField grundField = new JTextField(20);
    private final JComboBox<String> fahrerTypCombo = new JComboBox<>(new String[]{"Privat", "Firma"});
    private FahrtenbuchEintrag result = null;

    public FahrtenbuchDialog(Frame owner, String kennzeichen, FahrtenbuchService service) {
        super(owner, "Fahrtenbuch: " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.service = service;

        // Create toolbar with New button
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton("Neu");
        newButton.addActionListener(e -> showNewEntryDialog());
        toolBar.add(newButton);

        // Table setup
        table = new JTable(new FahrtenbuchTableModel(service.getEintraegeForFahrzeug(kennzeichen)));
        
        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    private void showNewEntryDialog() {
        JDialog dialog = new JDialog(this, "Neue Fahrt", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add components
        addComponent(mainPanel, new JLabel("Start:"), startOrtField, gbc, 0);
        addComponent(mainPanel, new JLabel("Ziel:"), zielOrtField, gbc, 1);
        addComponent(mainPanel, new JLabel("Kilometer:"), kilometerField, gbc, 2);
        
        // Add new components
        addComponent(mainPanel, new JLabel("Fahrer Typ:"), fahrerTypCombo, gbc, 3);
        addComponent(mainPanel, new JLabel("Fahrer Name:"), fahrerNameField, gbc, 4);
        addComponent(mainPanel, new JLabel("Grund:"), grundField, gbc, 5);
        
        // Pre-fill and disable kennzeichen field
        kennzeichenField.setText(kennzeichen);
        kennzeichenField.setEnabled(false);
        addComponent(mainPanel, new JLabel("Kennzeichen:"), kennzeichenField, gbc, 6);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        okButton.addActionListener(e -> {
            if (isInputValid()) {
                result = createFahrtenbuchEintrag();
                service.addEintrag(result);
                ((FahrtenbuchTableModel)table.getModel()).updateData(
                    service.getEintraegeForFahrzeug(kennzeichen)
                );
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Bitte füllen Sie alle Felder aus.", 
                    "Ungültige Eingabe", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
        return !startOrtField.getText().trim().isEmpty() &&
               !zielOrtField.getText().trim().isEmpty() &&
               !kilometerField.getText().trim().isEmpty() &&
               !kennzeichenField.getText().trim().isEmpty() &&
               !fahrerNameField.getText().trim().isEmpty() &&
               !grundField.getText().trim().isEmpty();
    }

    private FahrtenbuchEintrag createFahrtenbuchEintrag() {
        try {
            return new FahrtenbuchEintrag(
                LocalDate.now(),
                startOrtField.getText().trim(),
                zielOrtField.getText().trim(),
                Double.parseDouble(kilometerField.getText().trim()),
                kennzeichenField.getText().trim(),
                fahrerTypCombo.getSelectedItem().toString(),
                fahrerNameField.getText().trim(),
                grundField.getText().trim()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Bitte geben Sie eine gültige Kilometerzahl ein.",
                "Ungültige Eingabe",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public FahrtenbuchEintrag getResult() {
        return result;
    }
} 