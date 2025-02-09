package de.fuhrpark.model.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Repräsentiert einen LKW im Fuhrpark.
 * Erweitert die Basisklasse Fahrzeug um LKW-spezifische Funktionalität.
 */
public class LKW extends Fahrzeug {
    private final double ladekapazitaet;
    private final boolean hatAnhaengerkupplung;
    private double wert;

    /**
     * Konstruktor für einen LKW
     */
    public LKW(String kennzeichen, String marke, String modell, double wert) {
        super(kennzeichen, marke, modell, wert);
        this.ladekapazitaet = 40.0; // Default value in tons
        this.hatAnhaengerkupplung = true;
        this.wert = wert;
    }

    @Override
    public String getTyp() {
        return "LKW";
    }

    public double getLadekapazitaet() { return ladekapazitaet; }
    public boolean hasAnhaengerkupplung() { return hatAnhaengerkupplung; }

    @Override
    public double berechneAktuellenWert() {
        return getWert() * 0.85;
    }

    @Override
    public double getWert() {
        return wert;
    }

    @Override
    public void setWert(double wert) {
        this.wert = wert;
    }
} 