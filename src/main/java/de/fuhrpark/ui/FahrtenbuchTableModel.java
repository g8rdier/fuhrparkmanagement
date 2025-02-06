package de.fuhrpark.ui;

import de.fuhrpark.model.FahrtenbuchEintrag;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FahrtenbuchTableModel extends AbstractTableModel {
    private List<FahrtenbuchEintrag> eintraege;
    private final String[] columnNames = {"Datum", "Start", "Ziel", "Kilometer", "Fahrer"};

    public FahrtenbuchTableModel(List<FahrtenbuchEintrag> eintraege) {
        this.eintraege = eintraege;
    }

    @Override
    public int getRowCount() {
        return eintraege.size();
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
        FahrtenbuchEintrag eintrag = eintraege.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> eintrag.getDatum();
            case 1 -> eintrag.getStartOrt();
            case 2 -> eintrag.getZielOrt();
            case 3 -> eintrag.getKilometer();
            case 4 -> eintrag.getFahrer();
            default -> null;
        };
    }

    public void updateData(List<FahrtenbuchEintrag> newEintraege) {
        this.eintraege = newEintraege;
        fireTableDataChanged();
    }
} 