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
        // TODO: Implement add functionality
    }
}