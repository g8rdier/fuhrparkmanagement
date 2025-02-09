package de.fuhrpark.ui.model;

import javax.swing.table.AbstractTableModel;
import de.fuhrpark.model.base.Fahrzeug;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FahrzeugTableModel extends AbstractTableModel {
    private final List<Fahrzeug> fahrzeuge;
    private final String[] columnNames = {"Typ", "Kennzeichen", "Marke", "Modell", "Aktueller Wert"};
    private final DecimalFormat currencyFormat;

    public FahrzeugTableModel() {
        this.fahrzeuge = new ArrayList<>();
        this.currencyFormat = (DecimalFormat) NumberFormat.getInstance(Locale.GERMANY);
        this.currencyFormat.setMinimumFractionDigits(2);
        this.currencyFormat.setMaximumFractionDigits(2);
        this.currencyFormat.setGroupingUsed(true);
    }

    public void setFahrzeuge(List<Fahrzeug> fahrzeuge) {
        this.fahrzeuge.clear();
        this.fahrzeuge.addAll(fahrzeuge);
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
            case 0 -> fahrzeug.getTyp();
            case 1 -> fahrzeug.getKennzeichen();
            case 2 -> fahrzeug.getMarke();
            case 3 -> fahrzeug.getModell();
            case 4 -> currencyFormat.format(fahrzeug.getPreis()) + " €";
            default -> null;
        };
    }

    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.add(fahrzeug);
        fireTableRowsInserted(fahrzeuge.size() - 1, fahrzeuge.size() - 1);
    }

    public void updateFahrzeug(int row, Fahrzeug fahrzeug) {
        if (fahrzeug == null) {
            throw new IllegalArgumentException("Fahrzeug darf nicht null sein");
        }
        if (row < 0 || row >= fahrzeuge.size()) {
            throw new IndexOutOfBoundsException("Ungültiger Zeilenindex: " + row);
        }
        fahrzeuge.set(row, fahrzeug);
        fireTableRowsUpdated(row, row);
    }

    public void removeFahrzeug(int row) {
        if (row >= 0 && row < fahrzeuge.size()) {
            fahrzeuge.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    public Fahrzeug getFahrzeug(int row) {
        if (row < 0 || row >= fahrzeuge.size()) {
            throw new IndexOutOfBoundsException("Ungültiger Zeilenindex: " + row);
        }
        return fahrzeuge.get(row);
    }

    public void updateFahrzeuge(List<Fahrzeug> fahrzeuge) {
        setFahrzeuge(fahrzeuge);
    }
} 