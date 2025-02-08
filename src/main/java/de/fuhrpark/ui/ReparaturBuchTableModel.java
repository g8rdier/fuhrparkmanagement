package de.fuhrpark.ui;

import de.fuhrpark.model.ReparaturBuchEintrag;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ReparaturBuchTableModel extends AbstractTableModel {
    private List<ReparaturBuchEintrag> eintraege;
    private final String[] columnNames = {"Datum", "Beschreibung", "Kosten", "Werkstatt"};

    public ReparaturBuchTableModel(List<ReparaturBuchEintrag> eintraege) {
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
        ReparaturBuchEintrag eintrag = eintraege.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> eintrag.getDatumFormatted();
            case 1 -> eintrag.getBeschreibung();
            case 2 -> String.format("%.2f €", eintrag.getKosten());
            case 3 -> eintrag.getWerkstatt();
            default -> null;
        };
    }

    public void updateData(List<ReparaturBuchEintrag> newEintraege) {
        this.eintraege = newEintraege;
        fireTableDataChanged();
    }
} 