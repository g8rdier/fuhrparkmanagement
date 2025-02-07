package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FahrzeugTableModel extends AbstractTableModel {
    private List<Fahrzeug> fahrzeuge = new ArrayList<>();
    private final String[] columnNames = {
        "Kennzeichen", "Hersteller", "Modell", "Typ", "Baujahr", "Kilometerstand"
    };

    public void setFahrzeuge(List<Fahrzeug> fahrzeuge) {
        this.fahrzeuge = new ArrayList<>(fahrzeuge);
        fireTableDataChanged();
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
            case 1 -> fahrzeug.getMarke();
            case 2 -> fahrzeug.getModell();
            case 3 -> fahrzeug.getTyp();
            case 4 -> fahrzeug.getBaujahr();
            case 5 -> fahrzeug.getKilometerstand();
            default -> null;
        };
    }

    public Fahrzeug getFahrzeugAt(int rowIndex) {
        return fahrzeuge.get(rowIndex);
    }
} 