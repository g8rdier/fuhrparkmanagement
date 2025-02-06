package de.fuhrpark.model;

import java.time.LocalDate;

public class FahrtenbuchEintrag {
    private final String fahrzeugKennzeichen;
    private final LocalDate datum;
    private final String start;
    private final String ziel;
    private final int kilometer;
    private final String fahrer;

    public FahrtenbuchEintrag(String fahrzeugKennzeichen, LocalDate datum, 
                             String start, String ziel, int kilometer, String fahrer) {
        this.fahrzeugKennzeichen = fahrzeugKennzeichen;
        this.datum = datum;
        this.start = start;
        this.ziel = ziel;
        this.kilometer = kilometer;
        this.fahrer = fahrer;
    }

    // Getters
    public String getFahrzeugKennzeichen() { return fahrzeugKennzeichen; }
    public LocalDate getDatum() { return datum; }
    public String getStart() { return start; }
    public String getZiel() { return ziel; }
    public int getKilometer() { return kilometer; }
    public String getFahrer() { return fahrer; }
} 