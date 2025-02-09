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
        this.sitzplaetze = 5;
        this.hatKlimaanlage = true;
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
    public void setPreis(double preis) {
        super.setPreis(preis);
    }

    @Override
    public FahrzeugTyp getTyp() {
        return FahrzeugTyp.PKW;
    }

    public int getSitzplaetze() { return sitzplaetze; }
    public boolean hasKlimaanlage() { return hatKlimaanlage; }

    @Override
    public double berechneAktuellenWert() {
        return getPreis() * 0.9;
    }

    @Override
    public String toString() {
        return "PKW [Kennzeichen=" + getKennzeichen() + "]";
    }
} 