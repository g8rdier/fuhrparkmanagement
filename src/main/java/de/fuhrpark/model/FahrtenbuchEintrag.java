package de.fuhrpark.model;

import java.time.LocalDate;

public class FahrtenbuchEintrag {
    private final LocalDate datum;
    private final String startOrt;
    private final String zielOrt;
    private final double kilometer;
    private final String fahrzeugKennzeichen;
    private String fahrer;

    public FahrtenbuchEintrag(LocalDate datum, String startOrt, String zielOrt, 
                             double kilometer, String fahrzeugKennzeichen) {
        this.datum = datum;
        this.startOrt = startOrt;
        this.zielOrt = zielOrt;
        this.kilometer = kilometer;
        this.fahrzeugKennzeichen = fahrzeugKennzeichen;
    }

    // Getters
    public LocalDate getDatum() { return datum; }
    public String getStartOrt() { return startOrt; }
    public String getZielOrt() { return zielOrt; }
    public double getKilometer() { return kilometer; }
    public String getFahrzeugKennzeichen() { return fahrzeugKennzeichen; }

    // New methods required by UI
    public String getStart() { return startOrt; }
    public String getZiel() { return zielOrt; }
    public String getFahrer() { return fahrer; }
    public void setFahrer(String fahrer) { this.fahrer = fahrer; }
} 