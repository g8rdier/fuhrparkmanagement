package de.fuhrpark.model.base;

import java.io.Serializable;
import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Abstrakte Basisklasse für alle Fahrzeuge im Fuhrpark.
 * Definiert gemeinsame Eigenschaften und Verhalten.
 */
public class Fahrzeug implements Serializable {
    private String kennzeichen;
    private String marke;
    private String modell;
    private double wert;

    public Fahrzeug(String kennzeichen, String marke, String modell, double wert) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.wert = wert;
    }

    public String getKennzeichen() { return kennzeichen; }
    public String getMarke() { return marke; }
    public String getModell() { return modell; }
    public double getWert() { return wert; }
    public void setWert(double wert) { this.wert = wert; }
    public void setMarke(String marke) { this.marke = marke; }
    public void setModell(String modell) { this.modell = modell; }
    public String getTyp() { return "UNBEKANNT"; }
    public double berechneAktuellenWert() { return wert; }
}
