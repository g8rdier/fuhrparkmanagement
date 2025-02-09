package de.fuhrpark.model.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Repräsentiert einen PKW im Fuhrpark.
 * Erweitert die Basisklasse Fahrzeug um PKW-spezifische Funktionalität.
 */
public class PKW extends Fahrzeug {
    private final int sitzplaetze;
    private final boolean hatKlimaanlage;
    private double wert;

    /**
     * Konstruktor für einen PKW
     */
    public PKW(String kennzeichen, String marke, String modell, double wert) {
        super(kennzeichen, marke, modell, wert);
        this.sitzplaetze = 5;
        this.hatKlimaanlage = true;
        this.wert = wert;
    }

    @Override
    public double getWert() {
        return wert;
    }

    @Override
    public void setWert(double wert) {
        this.wert = wert;
    }

    @Override
    public void setMarke(String marke) {
        super.setMarke(marke);
    }

    @Override
    public void setModell(String modell) {
        super.setModell(modell);
    }

    @Override
    public FahrzeugTyp getTyp() {
        return FahrzeugTyp.PKW;
    }

    public int getSitzplaetze() { return sitzplaetze; }
    public boolean hasKlimaanlage() { return hatKlimaanlage; }

    @Override
    public double berechneAktuellenWert() {
        return getWert() * 0.9;
    }

    @Override
    public String toString() {
        return "PKW [Kennzeichen=" + getKennzeichen() + "]";
    }
} 