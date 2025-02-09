package de.fuhrpark.service.impl;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.service.base.FahrzeugFactory;

public class FahrzeugFactoryImpl implements FahrzeugFactory {
    @Override
    public Fahrzeug createFahrzeug(String typ, String kennzeichen, String marke, String modell, double preis) {
        return switch (typ) {
            case "PKW" -> new PKW(kennzeichen, marke, modell, preis);
            case "LKW" -> new LKW(kennzeichen, marke, modell, preis);
            default -> throw new IllegalArgumentException("Unbekannter Fahrzeugtyp: " + typ);
        };
    }
}