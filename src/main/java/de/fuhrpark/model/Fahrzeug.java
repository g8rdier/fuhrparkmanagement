package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Repräsentiert ein Fahrzeug im Fuhrpark.
 */
public abstract class Fahrzeug {
    protected String kennzeichen;
    private final String marke;
    private final String modell;
    private final FahrzeugTyp typ;
    private final int baujahr;
    private final double kilometerstand;
    private double grundpreis;
    private String status;
    private double aktuellerWert;

    // Konstruktor
    public Fahrzeug(String kennzeichen, String marke, String modell, 
                    FahrzeugTyp typ, int baujahr, double kilometerstand) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.typ = typ;
        this.baujahr = baujahr;
        this.kilometerstand = kilometerstand;
        this.grundpreis = 0.0; // Default grundpreis
        this.aktuellerWert = 0.0; // Default aktuellerWert
        this.status = "verfügbar";
    }

    // Getter/Setter
    public String getKennzeichen() {
        return kennzeichen;
    }

    public FahrzeugTyp getTyp() {
        return typ;
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

    public double getAktuellerWert() {
        return aktuellerWert;
    }

    public void setAktuellerWert(double wert) {
        this.aktuellerWert = wert;
    }

    public double getGrundpreis() {
        return grundpreis;
    }

    public void setGrundpreis(double grundpreis) {
        this.grundpreis = grundpreis;
    }

    public double getKilometerstand() {
        return kilometerstand;
    }

    /**
     * Prüft, ob das Fahrzeug verfügbar ist.
     * @return true, wenn der Status "verfügbar" ist.
     */
    public boolean isVerfuegbar() {
        return "verfügbar".equals(this.status);
    }
}
