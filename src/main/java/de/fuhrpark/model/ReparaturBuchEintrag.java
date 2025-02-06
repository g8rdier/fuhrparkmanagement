package de.fuhrpark.model;

import de.fuhrpark.model.enums.ReparaturTyp;
import java.time.LocalDate;

public class ReparaturBuchEintrag {
    private final LocalDate datum;
    private final ReparaturTyp typ;
    private final String beschreibung;
    private final double kosten;
    private final String fahrzeugKennzeichen;
    private final String werkstatt;

    public ReparaturBuchEintrag(LocalDate datum, ReparaturTyp typ, 
                               String beschreibung, double kosten, 
                               String fahrzeugKennzeichen, String werkstatt) {
        this.datum = datum;
        this.typ = typ;
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.fahrzeugKennzeichen = fahrzeugKennzeichen;
        this.werkstatt = werkstatt;
    }

    // Getters
    public String getFahrzeugKennzeichen() { return fahrzeugKennzeichen; }
    public LocalDate getDatum() { return datum; }
    public String getBeschreibung() { return beschreibung; }
    public double getKosten() { return kosten; }
    public String getWerkstatt() { return werkstatt; }
    public ReparaturTyp getTyp() { return typ; }
} 