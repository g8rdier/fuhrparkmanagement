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
import java.util.Vector;

public class FuhrparkUI extends JFrame {
    private final FahrzeugService fahrzeugService;
    private final FahrtenbuchService fahrtenbuchService;
    private final ReparaturService reparaturService;
    
    private final JTable fahrzeugTable;
    private final FahrzeugTableModel tableModel;

    // Custom TableModel class to handle Fahrzeug objects
    private class FahrzeugTableModel extends DefaultTableModel {
        private final String[] columnNames = {"Kennzeichen", "Typ", "Hersteller", "Modell"};
        private List<Fahrzeug> fahrzeuge;

        public FahrzeugTableModel() {
            super();
            this.fahrzeuge = List.of();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public int getRowCount() {
            return fahrzeuge == null ? 0 : fahrzeuge.size();
        }

        @Override
        public Object getValueAt(int row, int column) {
            Fahrzeug fahrzeug = fahrzeuge.get(row);
            return switch (column) {
                case 0 -> fahrzeug.getKennzeichen();
                case 1 -> fahrzeug.getTyp();
                case 2 -> fahrzeug.getMarke();
                case 3 -> fahrzeug.getModell();
                default -> null;
            };
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public void setFahrzeuge(List<Fahrzeug> fahrzeuge) {
            this.fahrzeuge = fahrzeuge;
            fireTableDataChanged();
        }

        public Fahrzeug getFahrzeugAt(int row) {
            return row >= 0 && row < fahrzeuge.size() ? fahrzeuge.get(row) : null;
        }
    }

    public FuhrparkUI(FahrzeugService fahrzeugService, 
                     FahrtenbuchService fahrtenbuchService,
                     ReparaturService reparaturService) {
        this.fahrzeugService = fahrzeugService;
        this.fahrtenbuchService = fahrtenbuchService;
        this.reparaturService = reparaturService;

        this.tableModel = new FahrzeugTableModel();
        this.fahrzeugTable = new JTable(tableModel);
        this.fahrzeugTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        toolBar.setFloatable(false);
        
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

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void refreshFahrzeugTable() {
        List<Fahrzeug> fahrzeuge = fahrzeugService.getAlleFahrzeuge();
        tableModel.setFahrzeuge(fahrzeuge);
    }

    private Fahrzeug getSelectedFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        return tableModel.getFahrzeugAt(selectedRow);
    }

    private void showAddFahrzeugDialog() {
        FahrzeugDialog dialog = new FahrzeugDialog(this, "Neues Fahrzeug", true);
        dialog.setVisible(true);
        Fahrzeug fahrzeug = dialog.getResult();
        if (fahrzeug != null) {
            fahrzeugService.saveFahrzeug(fahrzeug);
            refreshFahrzeugTable();
        }
    }

    private void showAddFahrtDialog() {
        Fahrzeug selectedFahrzeug = getSelectedFahrzeug();
        if (selectedFahrzeug == null) {
            JOptionPane.showMessageDialog(this, 
                "Bitte wählen Sie zuerst ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Will be implemented later
        JOptionPane.showMessageDialog(this, "Fahrtenbuch-Dialog wird implementiert");
    }

    private void showAddReparaturDialog() {
        Fahrzeug selectedFahrzeug = getSelectedFahrzeug();
        if (selectedFahrzeug == null) {
            JOptionPane.showMessageDialog(this, 
                "Bitte wählen Sie zuerst ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ReparaturDialog dialog = new ReparaturDialog(this, "Neue Reparatur", true);
        dialog.setVisible(true);
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
} 