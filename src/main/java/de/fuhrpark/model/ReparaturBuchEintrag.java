package de.fuhrpark.model;

import de.fuhrpark.model.enums.ReparaturTyp;
import java.time.LocalDate;

public class ReparaturBuchEintrag {
    private LocalDate datum;
    private ReparaturTyp typ;
    private String beschreibung;
    private double kosten;
    private String fahrzeugKennzeichen;

    public ReparaturBuchEintrag(LocalDate datum, ReparaturTyp typ, 
                               String beschreibung, double kosten, 
                               String fahrzeugKennzeichen) {
        this.datum = datum;
        this.typ = typ;
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.fahrzeugKennzeichen = fahrzeugKennzeichen;
    }

    // Getters
    public LocalDate getDatum() { return datum; }
    public ReparaturTyp getTyp() { return typ; }
    public String getBeschreibung() { return beschreibung; }
    public double getKosten() { return kosten; }
    public String getFahrzeugKennzeichen() { return fahrzeugKennzeichen; }
} 