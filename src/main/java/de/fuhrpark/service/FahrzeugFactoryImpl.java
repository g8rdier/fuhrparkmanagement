package de.fuhrpark.service.impl;

import de.fuhrpark.service.base.FahrzeugFactory;
import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;

public class FahrzeugFactoryImpl implements FahrzeugFactory {
    
    @Override
    public Fahrzeug createFahrzeug(String type, String kennzeichen, String marke, String modell, double preis) {
        if (type == null || kennzeichen == null) {
            throw new IllegalArgumentException("Typ und Kennzeichen dürfen nicht null sein");
        }
        
        switch (type.toUpperCase()) {
            case "PKW":
                return new PKW(kennzeichen, marke, modell, preis);
            case "LKW":
                return new LKW(kennzeichen, marke, modell, preis);
            default:
                throw new IllegalArgumentException("Unbekannter Fahrzeugtyp: " + type);
        }
    }
}