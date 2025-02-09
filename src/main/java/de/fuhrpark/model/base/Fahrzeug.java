package de.fuhrpark.model.base;

public class Fahrzeug {
    private final String kennzeichen;
    private String marke;
    private String modell;
    private double preis;
    private final String typ;

    public Fahrzeug(String kennzeichen, String marke, String modell, double preis, String typ) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.preis = preis;
        this.typ = typ;
    }

    public String getKennzeichen() { return kennzeichen; }
    public String getMarke() { return marke; }
    public void setMarke(String marke) { this.marke = marke; }
    public String getModell() { return modell; }
    public void setModell(String modell) { this.modell = modell; }
    public double getPreis() { return preis; }
    public void setPreis(double preis) { this.preis = preis; }
    public String getTyp() { return typ; }

    public double berechneAktuellenWert() {
        return switch (typ) {
            case "PKW" -> preis * 0.9;  // PKWs lose 10% value
            case "LKW" -> preis * 0.85; // LKWs lose 15% value
            default -> preis;
        };
    }
}
