package de.fuhrpark.model.impl;

import de.fuhrpark.model.base.Fahrzeug;

/**
 * Repräsentiert einen LKW im Fuhrpark.
 * Erweitert die Basisklasse Fahrzeug um LKW-spezifische Funktionalität.
 */
public class LKW extends Fahrzeug {
    private static final long serialVersionUID = 1L;
    private final double ladekapazitaet;
    private final boolean hatAnhaengerkupplung;
    private double wert;

    /**
     * Konstruktor für einen LKW
     */
    public LKW(String kennzeichen, String marke, String modell, double preis) {
        super(kennzeichen, marke, modell, preis, "LKW");
        this.ladekapazitaet = 40.0; // Default value in tons
        this.hatAnhaengerkupplung = true;
        this.wert = preis;
    }

    public double getLadekapazitaet() { return ladekapazitaet; }
    public boolean hasAnhaengerkupplung() { return hatAnhaengerkupplung; }

    @Override
    public double berechneAktuellenWert() {
        return getPreis() * 0.85;
    }

    @Override
    public double getPreis() {
        return wert;
    }

    @Override
    public void setPreis(double preis) {
        this.wert = preis;
    }
} 