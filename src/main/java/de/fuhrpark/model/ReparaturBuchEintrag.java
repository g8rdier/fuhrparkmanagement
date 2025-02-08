package de.fuhrpark.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReparaturBuchEintrag {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    private final String kennzeichen;
    private final double kosten;
    private final String beschreibung;
    private final String werkstatt;
    private LocalDate datum;

    public ReparaturBuchEintrag(String kennzeichen, double kosten, String beschreibung, String werkstatt, String datumStr) {
        this.kennzeichen = kennzeichen;
        this.kosten = kosten;
        this.beschreibung = beschreibung;
        this.werkstatt = werkstatt;
        this.datum = LocalDate.parse(datumStr, DATE_FORMATTER);
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public double getKosten() {
        return kosten;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getWerkstatt() {
        return werkstatt;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public String getDatumFormatted() {
        return datum.format(DATE_FORMATTER);
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setKosten(double kosten) {
        this.kosten = kosten;
    }

    public void setWerkstatt(String werkstatt) {
        this.werkstatt = werkstatt;
    }
} 