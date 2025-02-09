package de.fuhrpark.model.base;

import java.io.Serializable;
import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Abstrakte Basisklasse für alle Fahrzeuge im Fuhrpark.
 * Definiert gemeinsame Eigenschaften und Verhalten.
 */
public interface Fahrzeug extends Serializable {
    FahrzeugTyp getTyp();
    String getMarke();
    void setMarke(String marke);
    String getModell();
    void setModell(String modell);
    String getKennzeichen();
    double getPreis();
    void setPreis(double preis);
    double berechneAktuellenWert();
    double getWert();
    void setWert(double wert);
}
