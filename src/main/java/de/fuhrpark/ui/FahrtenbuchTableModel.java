package de.fuhrpark.ui;

import de.fuhrpark.model.FahrtenbuchEintrag;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FahrtenbuchTableModel extends AbstractTableModel {
    private List<FahrtenbuchEintrag> eintraege;
    private final String[] columnNames = {"Fahrer", "Datum", "Start", "Ziel", "Kilometer", "Grund"};

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
            case 0 -> eintrag.getFahrerName();
            case 1 -> eintrag.getDatum();
            case 2 -> eintrag.getStartOrt();
            case 3 -> eintrag.getZielOrt();
            case 4 -> String.format("%.1f", eintrag.getKilometer());
            case 5 -> eintrag.getGrund();
            default -> null;
        };
    }

    public void updateData(List<FahrtenbuchEintrag> newEintraege) {
        this.eintraege = newEintraege;
        fireTableDataChanged();
    }
} 