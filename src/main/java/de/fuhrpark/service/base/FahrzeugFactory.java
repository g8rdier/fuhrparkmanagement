package de.fuhrpark.service.base;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;

public interface FahrzeugFactory {
    Fahrzeug createFahrzeug(FahrzeugTyp typ, String kennzeichen, String marke, String modell, double preis);
} 