package de.fuhrpark.ui;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.model.FahrzeugTableModel;
import javax.swing.*;
import java.awt.*;

public class FuhrparkUI extends JFrame {
    private final FahrzeugFactory fahrzeugFactory;
    private final JTable fahrzeugTable;
    private final FahrzeugTableModel tableModel;

    public FuhrparkUI(FahrzeugFactory fahrzeugFactory) {
        super("Fuhrpark Verwaltung");
        this.fahrzeugFactory = fahrzeugFactory;
        this.tableModel = new FahrzeugTableModel();
        this.fahrzeugTable = new JTable(tableModel);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Create toolbar with add button
        JToolBar toolbar = new JToolBar();
        JButton addButton = new JButton("Fahrzeug hinzufügen");
        addButton.addActionListener(e -> addFahrzeug());
        toolbar.add(addButton);
        add(toolbar, BorderLayout.NORTH);

        // Add table in a scroll pane
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        add(scrollPane, BorderLayout.CENTER);

        // Set preferred size
        setPreferredSize(new Dimension(800, 600));
    }

    private void addFahrzeug() {
        FahrzeugDialog dialog = new FahrzeugDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                Fahrzeug fahrzeug = fahrzeugFactory.createFahrzeug(
                    dialog.getSelectedType(),
                    dialog.getKennzeichen(),
                    dialog.getMarke(),
                    dialog.getModell(),
                    dialog.getPreis()
                );

                tableModel.addFahrzeug(fahrzeug);
                tableModel.fireTableDataChanged();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Fehler beim Hinzufügen: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 
