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
    public LKW(String marke, String modell, String kennzeichen, double preis) {
        super(kennzeichen, marke, modell, preis);
        this.ladekapazitaet = 40.0; // Default value in tons
        this.hatAnhaengerkupplung = true;
        this.wert = preis;
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
        this.wert = preis;
    }

    @Override
    public FahrzeugTyp getTyp() {
        return FahrzeugTyp.LKW;
    }

    public double getLadekapazitaet() { return ladekapazitaet; }
    public boolean hasAnhaengerkupplung() { return hatAnhaengerkupplung; }

    @Override
    public double berechneAktuellenWert() {
        return getPreis() * 0.85;
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