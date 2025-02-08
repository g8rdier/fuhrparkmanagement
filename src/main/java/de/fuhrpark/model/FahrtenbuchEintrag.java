package de.fuhrpark.model;

import java.time.LocalDate;

public class FahrtenbuchEintrag {
    private LocalDate datum;
    private String startOrt;
    private String zielOrt;
    private double kilometer;
    private String kennzeichen;
    private String fahrer;
    private String fahrerTyp;
    private String fahrerName;
    private String grund;

    public FahrtenbuchEintrag(LocalDate datum, String startOrt, String zielOrt, 
                             double kilometer, String kennzeichen) {
        this.datum = datum;
        this.startOrt = startOrt;
        this.zielOrt = zielOrt;
        this.kilometer = kilometer;
        this.kennzeichen = kennzeichen;
    }

    public FahrtenbuchEintrag(LocalDate datum, String startOrt, String zielOrt, 
                             double kilometer, String kennzeichen, String fahrer) {
        this(datum, startOrt, zielOrt, kilometer, kennzeichen);
        this.fahrer = fahrer;
    }

    public FahrtenbuchEintrag(LocalDate datum, String startOrt, String zielOrt, 
                             double kilometer, String kennzeichen, 
                             String fahrerTyp, String fahrerName, String grund) {
        this.datum = datum;
        this.startOrt = startOrt;
        this.zielOrt = zielOrt;
        this.kilometer = kilometer;
        this.kennzeichen = kennzeichen;
        this.fahrerTyp = fahrerTyp;
        this.fahrerName = fahrerName;
        this.grund = grund;
    }

    // Getters
    public LocalDate getDatum() { return datum; }
    public String getStartOrt() { return startOrt; }
    public String getZielOrt() { return zielOrt; }
    public double getKilometer() { return kilometer; }
    public String getKennzeichen() { return kennzeichen; }
    public String getFahrer() { return fahrer; }
    public String getFahrerTyp() { return fahrerTyp; }
    public String getFahrerName() { return fahrerName; }
    public String getGrund() { return grund; }

    // New methods required by UI
    public String getStart() { return startOrt; }
    public String getZiel() { return zielOrt; }
    public void setFahrer(String fahrer) { this.fahrer = fahrer; }

    // Add getFahrzeugKennzeichen as an alias for getKennzeichen
    public String getFahrzeugKennzeichen() { return kennzeichen; }

    public void setFahrerTyp(String fahrerTyp) { this.fahrerTyp = fahrerTyp; }
    public void setFahrerName(String fahrerName) { this.fahrerName = fahrerName; }
    public void setGrund(String grund) { this.grund = grund; }
} 