package de.fuhrpark.ui;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.model.FahrzeugTableModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class FuhrparkUI extends JFrame {
    private final FahrzeugFactory fahrzeugFactory;
    private final JTable fahrzeugTable;
    private final FahrzeugTableModel tableModel;

    public FuhrparkUI(FahrzeugFactory fahrzeugFactory) {
        super("Fuhrpark Manager");
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.fahrzeugFactory = fahrzeugFactory;
        this.tableModel = new FahrzeugTableModel();
        this.fahrzeugTable = createTable();
        
        initComponents();
        setupFrame();
        
        // Add window listener to save data on exit
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                tableModel.saveData();
                System.exit(0);
            }
        });
    }

    private JTable createTable() {
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);  // Larger rows
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(false);  // Modern look without grid
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Fix the cell renderer implementation
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    label.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
                }
                
                return label;
            }
        };
        
        // Apply the renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        
        return table;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));  // Add spacing
        
        // Create modern toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBorder(new EmptyBorder(5, 5, 5, 5));
        toolbar.setBackground(new Color(240, 240, 240));

        // Create modern buttons
        JButton addButton = createStyledButton("Fahrzeug hinzufügen", "➕");
        JButton editButton = createStyledButton("Fahrzeug bearbeiten", "✎");
        JButton deleteButton = createStyledButton("Fahrzeug löschen", "🗑");
        
        addButton.addActionListener(e -> addFahrzeug());
        editButton.addActionListener(e -> editFahrzeug());
        deleteButton.addActionListener(e -> deleteFahrzeug());
        
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(addButton);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(editButton);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(deleteButton);
        
        add(toolbar, BorderLayout.NORTH);

        // Add table in a scroll pane with modern styling
        JScrollPane scrollPane = new JScrollPane(fahrzeugTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setBackground(new Color(250, 250, 250));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(250, 250, 250));
            }
        });
        
        return button;
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
    }

    private void addFahrzeug() {
        FahrzeugDialog dialog = new FahrzeugDialog(this, tableModel);
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

    private void editFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Fahrzeug fahrzeug = tableModel.getFahrzeug(selectedRow);
        FahrzeugDialog dialog = new FahrzeugDialog(this, tableModel, fahrzeug);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                // Update the vehicle with new values
                fahrzeug.setMarke(dialog.getMarke());
                fahrzeug.setModell(dialog.getModell());
                fahrzeug.setPreis(dialog.getPreis());
                
                tableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Fehler beim Bearbeiten: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteFahrzeug() {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Bitte wählen Sie ein Fahrzeug aus.",
                "Kein Fahrzeug ausgewählt",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Möchten Sie das ausgewählte Fahrzeug wirklich löschen?",
            "Fahrzeug löschen",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeFahrzeug(selectedRow);
            tableModel.fireTableDataChanged();
        }
    }
} 
