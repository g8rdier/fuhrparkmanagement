package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Repräsentiert einen LKW im Fuhrpark.
 * Erweitert die Basisklasse Fahrzeug um LKW-spezifische Funktionalität.
 */
public class LKW extends Fahrzeug {
    private final double ladekapazitaet;
    private final boolean hatAnhaengerkupplung;

    /**
     * Konstruktor für einen LKW
     */
    public LKW(String kennzeichen, String marke, String modell, 
              double ladekapazitaet, boolean hatAnhaengerkupplung) {
        super(kennzeichen, marke, modell, FahrzeugTyp.LKW);
        this.ladekapazitaet = ladekapazitaet;
        this.hatAnhaengerkupplung = hatAnhaengerkupplung;
    }

    public double getLadekapazitaet() { return ladekapazitaet; }
    public boolean hasAnhaengerkupplung() { return hatAnhaengerkupplung; }

    @Override
    public void berechneAktuellenWert() {
        // LKW-spezifische Wertberechnung
        double basisWert = getGrundpreis();
        if (hatAnhaengerkupplung) basisWert *= 1.15;
        setAktuellerWert(basisWert);
    }
} 