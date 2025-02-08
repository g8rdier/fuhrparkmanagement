package de.fuhrpark.model.impl;

import de.fuhrpark.model.base.AbstractFahrzeug;
import de.fuhrpark.model.base.FahrzeugTyp;

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
        super(FahrzeugTyp.LKW, marke, modell, kennzeichen, preis);
        this.ladekapazitaet = 0.0; // Assuming default value
        this.hatAnhaengerkupplung = false; // Assuming default value
    }

    public double getLadekapazitaet() { return ladekapazitaet; }
    public boolean hasAnhaengerkupplung() { return hatAnhaengerkupplung; }

    @Override
    public double berechneAktuellenWert() {
        // Simple depreciation calculation for LKW
        return getPreis() * 0.85; // 15% depreciation
    }
} 