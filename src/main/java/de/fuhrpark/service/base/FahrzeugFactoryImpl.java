package de.fuhrpark.service.impl;  // Changed from de.fuhrpark.service.impl to match expected package

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.base.FahrzeugTyp;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import de.fuhrpark.service.base.FahrzeugFactory;

public class FahrzeugFactoryImpl implements FahrzeugFactory {
    @Override
    public Fahrzeug erstelleFahrzeug(FahrzeugTyp typ, String marke, String modell, String kennzeichen, double preis) {
        return switch (typ) {
            case PKW -> new PKW(marke, modell, kennzeichen, preis);
            case LKW -> new LKW(marke, modell, kennzeichen, preis);
        };
    }
}