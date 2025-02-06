package de.fuhrpark.model;

import java.time.LocalDate;

public class FahrtenbuchEintrag {
    private LocalDate datum;
    private String fahrer;
    private String grund;
    private double kilometer;
    private String fahrzeugKennzeichen;

    public FahrtenbuchEintrag(LocalDate datum, String fahrer, String grund, 
                             double kilometer, String fahrzeugKennzeichen) {
        this.datum = datum;
        this.fahrer = fahrer;
        this.grund = grund;
        this.kilometer = kilometer;
        this.fahrzeugKennzeichen = fahrzeugKennzeichen;
    }

    // Getters
    public LocalDate getDatum() { return datum; }
    public String getFahrer() { return fahrer; }
    public String getGrund() { return grund; }
    public double getKilometer() { return kilometer; }
    public String getFahrzeugKennzeichen() { return fahrzeugKennzeichen; }
} 