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
    private double grundpreis;
    private String status;
    private double aktuellerWert;

    // Konstruktor
    public Fahrzeug(String kennzeichen, String marke, String modell, FahrzeugTyp typ) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.typ = typ;
        this.baujahr = 0; // Default baujahr
        this.aktuellerWert = 0.0; // Default aktuellerWert
        this.status = "verfügbar";
    }

    // Additional constructor with all parameters
    public Fahrzeug(String kennzeichen, String marke, String modell, FahrzeugTyp typ, 
                   int baujahr, double grundpreis) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.typ = typ;
        this.baujahr = baujahr;
        this.grundpreis = grundpreis;
        this.aktuellerWert = 0.0; // Default aktuellerWert
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

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public void setTyp(FahrzeugTyp typ) {
        this.typ = typ;
    }

    public double getGrundpreis() {
        return grundpreis;
    }

    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    public void setGrundpreis(double grundpreis) {
        this.grundpreis = grundpreis;
    }

    /**
     * Prüft, ob das Fahrzeug verfügbar ist.
     * @return true, wenn der Status "verfügbar" ist.
     */
    public boolean isVerfuegbar() {
        return "verfügbar".equals(this.status);
    }
}
