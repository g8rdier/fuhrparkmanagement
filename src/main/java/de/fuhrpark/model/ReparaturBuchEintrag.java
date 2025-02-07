package de.fuhrpark.model;

import java.time.LocalDate;

public class ReparaturBuchEintrag {
    private final String beschreibung;
    private final double kosten;
    private final String werkstatt;
    private final LocalDate datum;
    private final String kennzeichen;

    public ReparaturBuchEintrag(String beschreibung, double kosten, String werkstatt, LocalDate datum, String kennzeichen) {
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.werkstatt = werkstatt;
        this.datum = datum;
        this.kennzeichen = kennzeichen;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public double getKosten() {
        return kosten;
    }

    public String getWerkstatt() {
        return werkstatt;
    }

    public LocalDate getDatum() {
        return datum;
    }
    public String getKennzeichen() {
        return kennzeichen;
    }

} 