package de.fuhrpark.model.impl;

import de.fuhrpark.model.base.Fahrzeug;

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
    public PKW(String kennzeichen, String marke, String modell, double preis) {
        super(kennzeichen, marke, modell, preis, "PKW");
        this.sitzplaetze = 5;
        this.hatKlimaanlage = true;
    }

    public int getSitzplaetze() { 
        return sitzplaetze; 
    }
    
    public boolean hasKlimaanlage() { 
        return hatKlimaanlage; 
    }

    @Override
    public String toString() {
        return "PKW [Kennzeichen=" + getKennzeichen() + "]";
    }
} 