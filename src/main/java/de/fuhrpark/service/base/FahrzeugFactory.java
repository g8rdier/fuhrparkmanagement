package de.fuhrpark.service.base;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.enums.FahrzeugTyp;

public interface FahrzeugFactory {
    Fahrzeug erstelleFahrzeug(FahrzeugTyp typ, String marke, String modell, String kennzeichen, double preis);
} 