package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import de.fuhrpark.model.ReparaturBuchEintrag;
import de.fuhrpark.model.FahrtenbuchEintrag;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import de.fuhrpark.service.FahrzeugService;
import de.fuhrpark.service.FahrtenbuchService;
import de.fuhrpark.service.ReparaturService;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class FuhrparkUI extends JFrame {
    private final FahrzeugService fahrzeugService;
    private final FahrtenbuchService fahrtenbuchService;
    private final ReparaturService reparaturService;
    
    // Initialize all UI components
    private JTextField searchField;
    private JButton searchButton;
    private JButton newFahrzeugButton;
    private JButton fahrtenbuchButton;
    private JButton addReparaturButton;
    
    private JLabel kennzeichenLabel;
    private JLabel markeLabel;
    private JLabel modellLabel;
    private JLabel typLabel;
    private JLabel baujahrLabel;
    private JLabel kilometerstandLabel;
    
    private JTable fahrzeugTable;
    private JTable reparaturTable;
    private final DefaultTableModel reparaturTableModel;

    public FuhrparkUI(FahrzeugService fahrzeugService, FahrtenbuchService fahrtenbuchService, ReparaturService reparaturService) {
        super("Fuhrpark Verwaltung");
        this.fahrzeugService = fahrzeugService;
        this.fahrtenbuchService = fahrtenbuchService;
        this.reparaturService = reparaturService;
        
        // Initialize repair table model
        reparaturTableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Datum", "Beschreibung", "Kosten", "Werkstatt"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reparaturTable = new JTable(reparaturTableModel);
        
        initializeComponents();
        initializeUI();
    }

    private void initializeComponents() {
        // Initialize all components
        searchField = new JTextField(20);
        searchButton = new JButton("Fahrzeug suchen");
        newFahrzeugButton = new JButton("Neues Fahrzeug");
        fahrtenbuchButton = new JButton("Fahrtenbuch öffnen");
        addReparaturButton = new JButton("Reparatur hinzufügen");
        
        kennzeichenLabel = new JLabel("");
        markeLabel = new JLabel("");
        modellLabel = new JLabel("");
        typLabel = new JLabel("");
        baujahrLabel = new JLabel("");
        kilometerstandLabel = new JLabel("");
        
        fahrzeugTable = new JTable(new FahrzeugTableModel(fahrzeugService.getAlleFahrzeuge()));
    }

    private void initializeUI() {
        // Create main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Left panel with vehicle table
        JPanel leftPanel = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(fahrzeugTable);
        leftPanel.add(new JLabel("Fahrzeuge:"), BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Right panel with details and buttons
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createSearchPanel(), BorderLayout.NORTH);
        rightPanel.add(createDetailsPanel(), BorderLayout.CENTER);
        rightPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        // Add to frame
        add(splitPane);
        
        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        // Add selection listener to table
        fahrzeugTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = fahrzeugTable.getSelectedRow();
                if (row >= 0) {
                    String kennzeichen = (String) fahrzeugTable.getValueAt(row, 0);
                    updateFahrzeugDetails(fahrzeugService.getFahrzeug(kennzeichen));
                }
            }
        });
    }

    private void updateFahrzeugDetails(Fahrzeug fahrzeug) {
        if (fahrzeug != null) {
            kennzeichenLabel.setText(fahrzeug.getKennzeichen());
            markeLabel.setText(fahrzeug.getMarke());
            modellLabel.setText(fahrzeug.getModell());
            typLabel.setText(fahrzeug.getTyp().toString());
            baujahrLabel.setText(String.valueOf(fahrzeug.getBaujahr()));
            kilometerstandLabel.setText(String.valueOf(fahrzeug.getKilometerstand()));
            
            // Update repairs table
            updateReparaturTable(fahrzeug.getKennzeichen());
            
            List<FahrtenbuchEintrag> fahrten = fahrtenbuchService.getEintraegeForFahrzeug(fahrzeug.getKennzeichen());
            updateFahrtenList(fahrten);
        } else {
            clearLabels();
            updateReparaturTable(null);
        }
    }

    private void searchFahrzeug() {
        String kennzeichen = searchField.getText().trim();
        if (!kennzeichen.isEmpty()) {
            Fahrzeug fahrzeug = fahrzeugService.getFahrzeug(kennzeichen);
            if (fahrzeug != null) {
                updateFahrzeugDetails(fahrzeug);
                selectFahrzeug(kennzeichen);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Kein Fahrzeug mit diesem Kennzeichen gefunden.",
                    "Nicht gefunden",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void clearLabels() {
        kennzeichenLabel.setText("");
        markeLabel.setText("");
        modellLabel.setText("");
        typLabel.setText("");
        baujahrLabel.setText("");
        kilometerstandLabel.setText("");
        // Clear tables
        updateReparaturTable(null);
        updateFahrtenList(new ArrayList<>());
    }

    public void updateReparaturTable(String kennzeichen) {
        reparaturTableModel.setRowCount(0);
        if (kennzeichen != null) {
            List<ReparaturBuchEintrag> reparaturen = reparaturService.getReparaturenForFahrzeug(kennzeichen);
            for (ReparaturBuchEintrag reparatur : reparaturen) {
                reparaturTableModel.addRow(new Object[] {
                    reparatur.getDatumFormatted(),
                    reparatur.getBeschreibung(),
                    String.format("%.2f €", reparatur.getKosten()),
                    reparatur.getWerkstatt()
                });
            }
        }
    }

    private void handleAddReparatur() {
        String currentKennzeichen = kennzeichenLabel.getText();
        if (currentKennzeichen != null && !currentKennzeichen.isEmpty()) {
            ReparaturDialog dialog = new ReparaturDialog(this, "Neue Reparatur");
            dialog.setVisible(true);
            
            ReparaturBuchEintrag eintrag = dialog.getResult();
            if (eintrag != null) {
                reparaturService.addReparatur(currentKennzeichen, eintrag);
                updateReparaturTable(currentKennzeichen);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie zuerst ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private String getCurrentKennzeichen() {
        Fahrzeug selectedFahrzeug = getCurrentFahrzeug();
        return selectedFahrzeug != null ? selectedFahrzeug.getKennzeichen() : null;
    }

    private Fahrzeug getCurrentFahrzeug() {
        String kennzeichen = searchField.getText();
        return fahrzeugService.getFahrzeug(kennzeichen);
    }

    private void updateFahrtenList(List<FahrtenbuchEintrag> fahrten) {
        // Add code here to display the fahrtenbuch entries in your UI
        // For example, you might want to add another JTable or list to show them
    }

    private class FahrzeugTableModel extends AbstractTableModel {
        private final List<Fahrzeug> fahrzeuge;
        private final String[] columnNames = {"Kennzeichen", "Typ", "Hersteller", "Modell", "Baujahr"};

        public FahrzeugTableModel(List<Fahrzeug> fahrzeuge) {
            this.fahrzeuge = new ArrayList<>(fahrzeuge);
        }

        @Override
        public int getRowCount() {
            return fahrzeuge.size();
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
        public Object getValueAt(int rowIndex, int columnIndex) {
            Fahrzeug fahrzeug = fahrzeuge.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> fahrzeug.getKennzeichen();
                case 1 -> fahrzeug.getTyp();
                case 2 -> fahrzeug.getMarke();
                case 3 -> fahrzeug.getModell();
                case 4 -> fahrzeug.getBaujahr();
                default -> null;
            };
        }

        public void updateData(List<Fahrzeug> fahrzeuge) {
            this.fahrzeuge.clear();
            this.fahrzeuge.addAll(fahrzeuge);
            fireTableDataChanged();
        }
    }

    private void handleNewFahrzeug() {
        FahrzeugDialog dialog = new FahrzeugDialog(this, "Neues Fahrzeug", true);
        dialog.setVisible(true);
        
        Fahrzeug newFahrzeug = dialog.getResult();
        if (newFahrzeug != null) {
            fahrzeugService.addFahrzeug(newFahrzeug);
            // Update the table model with fresh data
            ((FahrzeugTableModel) fahrzeugTable.getModel()).updateData(
                fahrzeugService.getAlleFahrzeuge()
            );
            // Select the new vehicle
            selectFahrzeug(newFahrzeug.getKennzeichen());
            // Refresh the table display
            fahrzeugTable.repaint();
        }
    }

    private void selectFahrzeug(String kennzeichen) {
        for (int i = 0; i < fahrzeugTable.getRowCount(); i++) {
            if (kennzeichen.equals(fahrzeugTable.getValueAt(i, 0))) {
                fahrzeugTable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Suche"));
        
        searchField = new JTextField(20);
        searchButton = new JButton("Fahrzeug suchen");
        searchButton.addActionListener(e -> searchFahrzeug());
        
        searchPanel.add(new JLabel("Kennzeichen:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Fahrzeug Details"));
        
        // Create a panel for the labels using GridBagLayout
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add components to labelsPanel
        gbc.gridx = 0; gbc.gridy = 0;
        labelsPanel.add(new JLabel("Kennzeichen:"), gbc);
        gbc.gridx = 1;
        labelsPanel.add(kennzeichenLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        labelsPanel.add(new JLabel("Marke:"), gbc);
        gbc.gridx = 1;
        labelsPanel.add(markeLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        labelsPanel.add(new JLabel("Modell:"), gbc);
        gbc.gridx = 1;
        labelsPanel.add(modellLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        labelsPanel.add(new JLabel("Typ:"), gbc);
        gbc.gridx = 1;
        labelsPanel.add(typLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        labelsPanel.add(new JLabel("Baujahr:"), gbc);
        gbc.gridx = 1;
        labelsPanel.add(baujahrLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        labelsPanel.add(new JLabel("Kilometerstand:"), gbc);
        gbc.gridx = 1;
        labelsPanel.add(kilometerstandLabel, gbc);
        
        // Add panels with proper BorderLayout constraints
        detailsPanel.add(labelsPanel, BorderLayout.NORTH);
        
        // Add repairs panel
        JPanel reparaturPanel = new JPanel(new BorderLayout());
        reparaturPanel.setBorder(BorderFactory.createTitledBorder("Reparaturen"));
        reparaturPanel.add(new JScrollPane(reparaturTable), BorderLayout.CENTER);
        detailsPanel.add(reparaturPanel, BorderLayout.CENTER);
        
        return detailsPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        newFahrzeugButton = new JButton("Neues Fahrzeug");
        fahrtenbuchButton = new JButton("Fahrtenbuch öffnen");
        addReparaturButton = new JButton("Reparatur hinzufügen");
        
        newFahrzeugButton.addActionListener(e -> handleNewFahrzeug());
        fahrtenbuchButton.addActionListener(e -> handleFahrtenbuch());
        addReparaturButton.addActionListener(e -> handleAddReparatur());
        
        buttonPanel.add(newFahrzeugButton);
        buttonPanel.add(fahrtenbuchButton);
        buttonPanel.add(addReparaturButton);
        
        return buttonPanel;
    }

    private void handleFahrtenbuch() {
        String kennzeichen = getCurrentKennzeichen();
        if (kennzeichen != null) {
            FahrtenbuchDialog dialog = new FahrtenbuchDialog(this, kennzeichen, fahrtenbuchService);
            dialog.setVisible(true);
            // Refresh the view after dialog closes
            updateFahrzeugDetails(fahrzeugService.getFahrzeug(kennzeichen));
        } else {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie zuerst ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
        }
    }
} 
