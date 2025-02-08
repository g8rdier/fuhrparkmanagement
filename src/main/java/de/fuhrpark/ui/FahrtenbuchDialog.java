package de.fuhrpark.ui;

import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.service.FahrtenbuchService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FahrtenbuchDialog extends JDialog {
    private final FahrtenbuchService fahrtenbuchService;
    private final String kennzeichen;

    public FahrtenbuchDialog(Frame owner, String kennzeichen, FahrtenbuchService fahrtenbuchService) {
        super(owner, "Fahrtenbuch - " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.fahrtenbuchService = fahrtenbuchService;
        
        initializeUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create table model with data
        List<FahrtenbuchEintrag> eintraege = fahrtenbuchService.getEintraegeForFahrzeug(kennzeichen);
        FahrtenbuchTableModel model = new FahrtenbuchTableModel(eintraege);
        JTable table = new JTable(model);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Neue Fahrt");
        addButton.addActionListener(e -> handleAddFahrt());
        buttonPanel.add(addButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set dialog size
        setSize(600, 400);
    }

    private void handleAddFahrt() {
        JTextField startOrtField = new JTextField(20);
        JTextField zielOrtField = new JTextField(20);
        JTextField kilometerField = new JTextField(10);
        JTextField fahrerField = new JTextField(20);
        JTextField grundField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Start Ort:"));
        panel.add(startOrtField);
        panel.add(new JLabel("Ziel Ort:"));
        panel.add(zielOrtField);
        panel.add(new JLabel("Kilometer:"));
        panel.add(kilometerField);
        panel.add(new JLabel("Fahrer:"));
        panel.add(fahrerField);
        panel.add(new JLabel("Grund:"));
        panel.add(grundField);

        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Neue Fahrt", JOptionPane.OK_CANCEL_OPTION);
            
        if (result == JOptionPane.OK_OPTION) {
            try {
                double kilometer = Double.parseDouble(kilometerField.getText());
                
                FahrtenbuchEintrag eintrag = new FahrtenbuchEintrag(
                    LocalDate.now(),
                    startOrtField.getText(),
                    zielOrtField.getText(),
                    kilometer,
                    kennzeichen,
                    fahrerField.getText()
                );
                eintrag.setGrund(grundField.getText());

                fahrtenbuchService.addFahrt(kennzeichen, eintrag);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Bitte geben Sie eine gültige Zahl für die Kilometer ein.",
                    "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        // Refresh the table with updated data
        List<FahrtenbuchEintrag> eintraege = fahrtenbuchService.getEintraegeForFahrzeug(kennzeichen);
        ((FahrtenbuchTableModel) ((JTable) ((JScrollPane) getContentPane()
            .getComponent(0)).getViewport().getView()).getModel())
            .updateData(eintraege);
    }
}