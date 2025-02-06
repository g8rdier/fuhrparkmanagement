package de.fuhrpark.ui;

import javax.swing.table.DefaultTableModel;
import de.fuhrpark.model.Fahrzeug;
import java.util.List;

public class FahrzeugTableModel extends DefaultTableModel {
    
    public FahrzeugTableModel(String[] columnNames) {
        super(columnNames, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Make table read-only
    }

    public void updateData(List<Fahrzeug> fahrzeuge) {
        setRowCount(0);
        for (Fahrzeug fahrzeug : fahrzeuge) {
            addRow(new Object[]{
                fahrzeug.getKennzeichen(),
                fahrzeug.getMarke(),
                fahrzeug.getModell(),
                fahrzeug.getTyp().toString(),
                fahrzeug.getBaujahr(),
                fahrzeug.getStatus(),
                String.format("%.2f €", fahrzeug.getAktuellerWert())
            });
        }
        fireTableDataChanged();
    }
} 