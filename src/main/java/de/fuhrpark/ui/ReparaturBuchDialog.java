package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.service.ReparaturService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReparaturBuchDialog extends JDialog {
    private final JTable table;
    private final String kennzeichen;
    private final ReparaturService service;

    public ReparaturBuchDialog(Frame owner, String kennzeichen, ReparaturService service) {
        super(owner, "Reparaturbuch: " + kennzeichen, true);
        this.kennzeichen = kennzeichen;
        this.service = service;

        // Create toolbar with New button
        JToolBar toolBar = new JToolBar();
        JButton newButton = new JButton("Neu");
        newButton.addActionListener(e -> showNewEntryDialog());
        toolBar.add(newButton);

        // Table setup
        table = new JTable(new ReparaturBuchTableModel(service.getReparaturenForFahrzeug(kennzeichen)));
        
        // Layout
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    private void showNewEntryDialog() {
        ReparaturDialog dialog = new ReparaturDialog(
            (JFrame) getOwner(), 
            "Neue Reparatur"
        );
        dialog.setVisible(true);
        
        ReparaturBuchEintrag eintrag = dialog.getResult();
        if (eintrag != null) {
            service.addReparatur(kennzeichen, eintrag);
            updateTable();
        }
    }

    private void updateTable() {
        List<ReparaturBuchEintrag> eintraege = service.getReparaturenForFahrzeug(kennzeichen);
        ((ReparaturBuchTableModel)table.getModel()).updateData(eintraege);
    }
} 