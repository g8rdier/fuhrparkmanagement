package de.fuhrpark.ui.model;

public class FahrzeugData {
    private String typ;
    private String marke;
    private String modell;
    private String kennzeichen;
    private String preis;

    public FahrzeugData(String typ, String marke, String modell, String kennzeichen, String preis) {
        this.typ = typ;
        this.marke = marke;
        this.modell = modell;
        this.kennzeichen = kennzeichen;
        this.preis = preis;
    }

    public String getTyp() { return typ; }
    public String getMarke() { return marke; }
    public String getModell() { return modell; }
    public String getKennzeichen() { return kennzeichen; }
    public String getPreis() { return preis; }

    public void setTyp(String typ) { this.typ = typ; }
    public void setMarke(String marke) { this.marke = marke; }
    public void setModell(String modell) { this.modell = modell; }
    public void setKennzeichen(String kennzeichen) { this.kennzeichen = kennzeichen; }
    public void setPreis(String preis) { this.preis = preis; }
} 