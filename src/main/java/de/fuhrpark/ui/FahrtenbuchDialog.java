package de.fuhrpark.ui;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.service.FahrtenbuchService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FahrtenbuchDialog extends JDialog {
    private final String kennzeichen;
    private final FahrtenbuchService service;
    private final JTable table;
    private FahrtenbuchEintrag result;
    private final JTextField startOrtField = new JTextField(20);
    private final JTextField zielOrtField = new JTextField(20);
    private final JTextField kilometerField = new JTextField(20);
    private final JTextField kennzeichenField = new JTextField(20);
    private final JTextField fahrerNameField = new JTextField(20);
    private final JTextField firmaNameField = new JTextField(20);
    private final JTextField grundField = new JTextField(20);
    private final JComboBox<String> fahrerTypCombo = new JComboBox<>(new String[]{"Privat", "Firma"});
    private final JPanel namePanel = new JPanel(new CardLayout());

    public FahrtenbuchDialog(Frame owner, String kennzeichen, FahrtenbuchService service) {
        super(owner, "Fahrtenbuch: " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.service = service;
        
        // Create table with data
        table = new JTable(new FahrtenbuchTableModel(service.getFahrtenForFahrzeug(kennzeichen)));
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Toolbar with New button
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton("Neu");
        newButton.addActionListener(e -> showNewEntryDialog());
        toolBar.add(newButton);

        // Add components
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setSize(800, 400);
        setLocationRelativeTo(getOwner());
    }

    private void showNewEntryDialog() {
        JDialog dialog = new JDialog(this, "Neue Fahrt", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Add components
        mainPanel.add(new JLabel("Start:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(startOrtField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Ziel:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(zielOrtField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Kilometer:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(kilometerField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Fahrer Typ:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(fahrerTypCombo, gbc);

        // Setup name panel with card layout
        namePanel.add(fahrerNameField, "Privat");
        namePanel.add(firmaNameField, "Firma");

        fahrerTypCombo.addActionListener(e -> {
            CardLayout cl = (CardLayout) namePanel.getLayout();
            cl.show(namePanel, fahrerTypCombo.getSelectedItem().toString());
        });

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(namePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Grund:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(grundField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        okButton.addActionListener(e -> {
            if (isInputValid()) {
                result = createFahrtenbuchEintrag();
                if (result != null) {
                    dialog.dispose();
                }
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
            String selectedName = fahrerTypCombo.getSelectedItem().toString().equals("Privat") 
                ? fahrerNameField.getText().trim() 
                : firmaNameField.getText().trim();

            return new FahrtenbuchEintrag(
                LocalDate.now(),
                startOrtField.getText().trim(),
                zielOrtField.getText().trim(),
                Double.parseDouble(kilometerField.getText().trim()),
                kennzeichenField.getText().trim(),
                fahrerTypCombo.getSelectedItem().toString(),
                selectedName,
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