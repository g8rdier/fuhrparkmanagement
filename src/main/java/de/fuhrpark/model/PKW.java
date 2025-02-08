package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Repräsentiert einen PKW im Fuhrpark.
 * Erweitert die Basisklasse Fahrzeug um PKW-spezifische Funktionalität.
 */
public class PKW extends Fahrzeug {
    private final int sitzplaetze;
    private final boolean hatKlimaanlage;

    /**
     * Konstruktor für einen PKW
     */
    public PKW(String kennzeichen, String marke, String modell, 
               int sitzplaetze, boolean hatKlimaanlage) {
        super(kennzeichen, marke, modell, FahrzeugTyp.PKW);
        this.sitzplaetze = sitzplaetze;
        this.hatKlimaanlage = hatKlimaanlage;
    }

    public int getSitzplaetze() { return sitzplaetze; }
    public boolean hasKlimaanlage() { return hatKlimaanlage; }

    @Override
    public void berechneAktuellenWert() {
        // PKW-spezifische Wertberechnung
        double basisWert = getGrundpreis();
        if (hatKlimaanlage) basisWert *= 1.1;
        setAktuellerWert(basisWert);
    }

    @Override
    public FahrzeugTyp getTyp() {
        return FahrzeugTyp.PKW;
    }

    @Override
    public String toString() {
        return "PKW [Kennzeichen=" + kennzeichen + "]";
    }
} 