package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ReparaturTableModel extends AbstractTableModel {
    private List<ReparaturBuchEintrag> reparaturen;
    private final String[] columnNames = {"Datum", "Typ", "Beschreibung", "Kosten", "Werkstatt"};

    public ReparaturTableModel(List<ReparaturBuchEintrag> reparaturen) {
        this.reparaturen = reparaturen;
    }

    @Override
    public int getRowCount() {
        return reparaturen.size();
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
        ReparaturBuchEintrag reparatur = reparaturen.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> reparatur.getDatum();
            case 1 -> reparatur.getKennzeichen();
            case 2 -> reparatur.getBeschreibung();
            case 3 -> reparatur.getKosten();
            case 4 -> reparatur.getWerkstatt();
            default -> null;
        };
    }

    public void updateData(List<ReparaturBuchEintrag> newReparaturen) {
        this.reparaturen = newReparaturen;
        fireTableDataChanged();
    }
} 