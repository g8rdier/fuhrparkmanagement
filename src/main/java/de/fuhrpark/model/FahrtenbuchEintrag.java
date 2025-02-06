package de.fuhrpark.model;

import java.time.LocalDate;

public class FahrtenbuchEintrag {
    private LocalDate datum;
    private String startOrt;
    private String zielOrt;
    private double kilometer;
    private String kennzeichen;
    private String fahrer;

    public FahrtenbuchEintrag(LocalDate datum, String startOrt, String zielOrt, 
                             double kilometer, String kennzeichen, String fahrer) {
        this.datum = datum;
        this.startOrt = startOrt;
        this.zielOrt = zielOrt;
        this.kilometer = kilometer;
        this.kennzeichen = kennzeichen;
        this.fahrer = fahrer;
    }

    // Getters
    public LocalDate getDatum() { return datum; }
    public String getStartOrt() { return startOrt; }
    public String getZielOrt() { return zielOrt; }
    public double getKilometer() { return kilometer; }
    public String getKennzeichen() { return kennzeichen; }
    public String getFahrer() { return fahrer; }

    // New methods required by UI
    public String getStart() { return startOrt; }
    public String getZiel() { return zielOrt; }
    public void setFahrer(String fahrer) { this.fahrer = fahrer; }
} 