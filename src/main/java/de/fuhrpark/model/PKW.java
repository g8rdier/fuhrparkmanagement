package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

public class PKW extends Fahrzeug {
    
    public PKW(String kennzeichen) {
        super();  // Explicitly call the parent's default constructor
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