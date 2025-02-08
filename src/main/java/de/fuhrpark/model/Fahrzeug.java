package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;

/**
 * Abstrakte Basisklasse für alle Fahrzeuge im Fuhrpark.
 * Definiert gemeinsame Eigenschaften und Verhalten.
 */
public abstract class Fahrzeug {
    protected String kennzeichen;
    protected final String marke;
    protected final String modell;
    protected final FahrzeugTyp typ;
    protected double grundpreis;
    protected String status;
    protected double aktuellerWert;

    /**
     * Basiskonstruktor für alle Fahrzeuge
     */
    protected Fahrzeug(String kennzeichen, String marke, String modell, FahrzeugTyp typ) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.typ = typ;
        this.status = "verfügbar";
        this.aktuellerWert = 0.0;
    }

    // Getter/Setter mit deutschen Kommentaren
    public String getKennzeichen() { return kennzeichen; }
    public String getMarke() { return marke; }
    public String getModell() { return modell; }
    public FahrzeugTyp getTyp() { return typ; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getAktuellerWert() { return aktuellerWert; }
    public void setAktuellerWert(double wert) { this.aktuellerWert = wert; }
    
    public double getGrundpreis() { return grundpreis; }
    public void setGrundpreis(double grundpreis) { this.grundpreis = grundpreis; }

    /**
     * Berechnet den aktuellen Wert des Fahrzeugs.
     * Muss von konkreten Fahrzeugtypen implementiert werden.
     */
    public abstract void berechneAktuellenWert();

    /**
     * Prüft die Verfügbarkeit des Fahrzeugs
     */
    public boolean istVerfuegbar() {
        return "verfügbar".equals(this.status);
    }
}
