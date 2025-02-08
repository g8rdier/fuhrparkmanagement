package de.fuhrpark.model.base;

import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Abstrakte Basisklasse für alle Fahrzeuge im Fuhrpark.
 * Definiert gemeinsame Eigenschaften und Verhalten.
 */
public interface Fahrzeug {
    FahrzeugTyp getTyp();
    String getMarke();
    String getModell();
    String getKennzeichen();
    double getPreis();
    double berechneAktuellenWert();
}
