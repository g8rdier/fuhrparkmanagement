package de.fuhrpark.ui;

import de.fuhrpark.manager.FuhrparkManager;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.model.enums.FahrzeugTyp;
import de.fuhrpark.persistence.repository.impl.FileDataStore;
import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.service.base.FahrzeugService;
import de.fuhrpark.service.impl.FahrzeugFactoryImpl;
import de.fuhrpark.service.impl.FahrzeugServiceImpl;
import de.fuhrpark.ui.dialog.FahrzeugDialog;
import de.fuhrpark.ui.dialog.FahrzeugEditDialog;
import de.fuhrpark.ui.model.FahrzeugTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

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
        // ... layout code ...
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

    private void editFahrzeug(ActionEvent e) {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedRow = fahrzeugTable.convertRowIndexToModel(selectedRow);
            Fahrzeug fahrzeug = tableModel.getFahrzeug(selectedRow);
            FahrzeugDialog dialog = new FahrzeugDialog(this, fahrzeug);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                tableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            }
        }
    }

    private void deleteFahrzeug(ActionEvent e) {
        int selectedRow = fahrzeugTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedRow = fahrzeugTable.convertRowIndexToModel(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Möchten Sie das ausgewählte Fahrzeug wirklich löschen?",
                "Fahrzeug löschen",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeFahrzeug(selectedRow);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create components with proper error handling
                FileDataStore dataStore = new FileDataStore();
                dataStore.load();
                
                new FuhrparkUI(dataStore).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Fehler beim Starten der Anwendung: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
} 
