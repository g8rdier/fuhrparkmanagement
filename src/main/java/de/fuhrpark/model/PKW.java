package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

public class PKW extends Fahrzeug {
    
    public PKW(String kennzeichen) {
        super(kennzeichen, "", "", FahrzeugTyp.PKW, 0, 0.0);
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