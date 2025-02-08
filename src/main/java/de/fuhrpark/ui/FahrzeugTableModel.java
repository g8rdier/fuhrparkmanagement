package de.fuhrpark.ui;

import de.fuhrpark.model.Fahrzeug;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class FahrzeugTableModel extends AbstractTableModel {
    private final List<Fahrzeug> fahrzeuge = new ArrayList<>();
    private final String[] columnNames = {"Typ", "Kennzeichen", "Marke", "Modell", "Preis"};

    @Override
    public int getRowCount() {
        return fahrzeuge.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Fahrzeug f = fahrzeuge.get(row);
        switch (col) {
            case 0: return f.getTyp();
            case 1: return f.getKennzeichen();
            case 2: return f.getMarke();
            case 3: return f.getModell();
            case 4: return f.getGrundpreis();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.add(fahrzeug);
        fireTableRowsInserted(fahrzeuge.size()-1, fahrzeuge.size()-1);
    }

    public void updateFahrzeug(int row, Fahrzeug fahrzeug) {
        fahrzeuge.set(row, fahrzeug);
        fireTableRowsUpdated(row, row);
    }

    public void removeFahrzeug(int row) {
        fahrzeuge.remove(row);
        fireTableRowsDeleted(row, row);
    }
} 