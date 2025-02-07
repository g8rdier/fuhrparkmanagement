package de.fuhrpark.ui;

import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.model.FahrtenbuchEintrag;
import de.fuhrpark.model.ReparaturBuchEintrag;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FuhrparkUI extends JFrame {
    private final FahrzeugService fahrzeugService;
    private final FahrtenbuchService fahrtenbuchService;
    private final ReparaturService reparaturService;
    
    private final DefaultTableModel tableModel;
    private final JTable fahrzeugTable;

    public FuhrparkUI(FahrzeugService fahrzeugService, 
                     FahrtenbuchService fahrtenbuchService,
                     ReparaturService reparaturService) {
        this.fahrzeugService = fahrzeugService;
        this.fahrtenbuchService = fahrtenbuchService;
        this.reparaturService = reparaturService;

        // Initialize table model and table
        this.tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Kennzeichen", "Typ", "Hersteller", "Modell"}
        );
        this.fahrzeugTable = new JTable(tableModel);

        setTitle("Fuhrpark Verwaltung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        refreshFahrzeugTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");
        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener((ActionEvent e) -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Create toolbar
        JToolBar toolBar = new JToolBar();
        JButton addFahrzeugButton = new JButton("Neues Fahrzeug");
        addFahrzeugButton.addActionListener((ActionEvent e) -> showAddFahrzeugDialog());
        toolBar.add(addFahrzeugButton);

        JButton addFahrtButton = new JButton("Neue Fahrt");
        addFahrtButton.addActionListener((ActionEvent e) -> showAddFahrtDialog());
        toolBar.add(addFahrtButton);

        JButton addReparaturButton = new JButton("Neue Reparatur");
        addReparaturButton.addActionListener((ActionEvent e) -> showAddReparaturDialog());
        toolBar.add(addReparaturButton);

        add(toolBar, BorderLayout.NORTH);

        // Add table with scroll pane
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void refreshFahrzeugTable() {
        tableModel.setRowCount(0);
        for (Fahrzeug fahrzeug : fahrzeugService.getAlleFahrzeuge()) {
            tableModel.addRow(new Object[]{
                fahrzeug.getKennzeichen(),
                fahrzeug.getTyp(),
                fahrzeug.getHersteller(),
                fahrzeug.getModell()
            });
        }
    }

    private void showAddFahrzeugDialog() {
        // Implementation
    }

    private void showAddFahrtDialog() {
        // Implementation
    }

    private void showAddReparaturDialog() {
        // Implementation
    }

    private void showLogbookView() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Fahrzeug aus.");
            return;
        }

        Fahrzeug fahrzeug = tableModel.getFahrzeugAt(selectedRow);
        List<FahrtenbuchEintrag> eintraege = 
            fahrtenbuchService.getEintraegeForFahrzeug(fahrzeug.getKennzeichen());

        JDialog dialog = new JDialog(this, "Fahrtenbuch: " + fahrzeug.getKennzeichen(), true);
        dialog.setLayout(new BorderLayout());
        
        // Create table model and table for logbook entries
        String[] columns = {"Datum", "Start", "Ziel", "Kilometer", "Fahrer"};
        Object[][] data = eintraege.stream()
            .map(e -> new Object[]{e.getDatum(), e.getStart(), e.getZiel(), 
                                 e.getKilometer(), e.getFahrer()})
            .toArray(Object[][]::new);

        JTable logTable = new JTable(data, columns);
        dialog.add(new JScrollPane(logTable), BorderLayout.CENTER);
        
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showRepairView() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Fahrzeug aus.");
            return;
        }

        Fahrzeug selectedFahrzeug = tableModel.getFahrzeugAt(selectedRow);
        List<ReparaturBuchEintrag> reparaturen = 
            reparaturService.getReparaturenForFahrzeug(selectedFahrzeug.getKennzeichen());

        JDialog dialog = new JDialog(this, "Reparaturen: " + selectedFahrzeug.getKennzeichen(), true);
        dialog.setLayout(new BorderLayout());
        
        String[] columns = {"Datum", "Typ", "Beschreibung", "Kosten", "Werkstatt"};
        Object[][] data = reparaturen.stream()
            .map(r -> new Object[]{
                r.getDatum(), 
                r.getTyp(),
                r.getBeschreibung(), 
                String.format("%.2f €", r.getKosten()), 
                r.getWerkstatt()
            })
            .toArray(Object[][]::new);

        JTable repairTable = new JTable(data, columns);
        dialog.add(new JScrollPane(repairTable), BorderLayout.CENTER);
        
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void handleAddFahrzeug(Fahrzeug fahrzeug) {
        fahrzeugService.addFahrzeug(fahrzeug);
    }
} 