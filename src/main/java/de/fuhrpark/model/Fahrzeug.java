package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Repräsentiert ein Fahrzeug im Fuhrpark.
 */
public class Fahrzeug {
    private String kennzeichen;
    private String marke;
    private String modell;
    private FahrzeugTyp typ;
    private int baujahr;
    private String status;
    private double aktuellerWert;

    // Konstruktor
    public Fahrzeug(String kennzeichen, String marke, String modell, 
                   FahrzeugTyp typ, int baujahr, double initialerWert) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.typ = typ;
        this.baujahr = baujahr;
        this.aktuellerWert = initialerWert;
        this.status = "verfügbar";
    }

    // Getter/Setter
    public String getKennzeichen() {
        return kennzeichen;
    }

    public String getMarke() {
        return marke;
    }

    public String getModell() {
        return modell;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FahrzeugTyp getTyp() {
        return typ;
    }

    public double getAktuellerWert() {
        return aktuellerWert;
    }

    public void setAktuellerWert(double wert) {
        this.aktuellerWert = wert;
    }

    /**
     * Prüft, ob das Fahrzeug verfügbar ist.
     * @return true, wenn der Status "verfügbar" ist.
     */
    public boolean isVerfuegbar() {
        return "verfügbar".equals(this.status);
    }
}
