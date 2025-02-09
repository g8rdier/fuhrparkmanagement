package de.fuhrpark.model.impl;

import de.fuhrpark.model.base.AbstractFahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Repräsentiert einen LKW im Fuhrpark.
 * Erweitert die Basisklasse Fahrzeug um LKW-spezifische Funktionalität.
 */
public class LKW extends AbstractFahrzeug {
    private final double ladekapazitaet;
    private final boolean hatAnhaengerkupplung;

    /**
     * Konstruktor für einen LKW
     */
    public LKW(String marke, String modell, String kennzeichen, double preis) {
        super(marke, modell, kennzeichen, preis);
        this.ladekapazitaet = 40.0; // Default value in tons
        this.hatAnhaengerkupplung = true;
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
        return FahrzeugTyp.LKW;
    }

    public double getLadekapazitaet() { return ladekapazitaet; }
    public boolean hasAnhaengerkupplung() { return hatAnhaengerkupplung; }

    @Override
    public double berechneAktuellenWert() {
        return getPreis() * 0.85;
    }
} 