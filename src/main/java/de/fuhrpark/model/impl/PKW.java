package de.fuhrpark.model.impl;

import de.fuhrpark.model.base.AbstractFahrzeug;
import de.fuhrpark.model.base.FahrzeugTyp;

/**
 * Repräsentiert einen PKW im Fuhrpark.
 * Erweitert die Basisklasse Fahrzeug um PKW-spezifische Funktionalität.
 */
public class PKW extends AbstractFahrzeug {
    private final int sitzplaetze;
    private final boolean hatKlimaanlage;

    /**
     * Konstruktor für einen PKW
     */
    public PKW(String marke, String modell, String kennzeichen, double preis) {
        super(FahrzeugTyp.PKW, marke, modell, kennzeichen, preis);
        this.sitzplaetze = 0; // Default value, actual implementation needed
        this.hatKlimaanlage = false; // Default value, actual implementation needed
    }

    public int getSitzplaetze() { return sitzplaetze; }
    public boolean hasKlimaanlage() { return hatKlimaanlage; }

    @Override
    public double berechneAktuellenWert() {
        // Simple depreciation calculation for PKW
        return getPreis() * 0.9; // 10% depreciation
    }

    @Override
    public FahrzeugTyp getTyp() {
        return FahrzeugTyp.PKW;
    }

    @Override
    public String toString() {
        return "PKW [Kennzeichen=" + getKennzeichen() + "]";
    }
} 