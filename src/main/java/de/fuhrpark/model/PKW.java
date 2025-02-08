package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

public class PKW extends Fahrzeug {
    
    public PKW(String kennzeichen) {
        this.kennzeichen = kennzeichen;
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