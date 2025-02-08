package de.fuhrpark.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReparaturBuchEintrag {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    private String kennzeichen;
    private double kosten;
    private String beschreibung;
    private String werkstatt;
    private LocalDate datum;

    public ReparaturBuchEintrag(LocalDate datum, String beschreibung, double kosten, String werkstatt) {
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.werkstatt = werkstatt;
    }

    // Alternative constructor for string date input
    public ReparaturBuchEintrag(String datumStr, String beschreibung, double kosten, String werkstatt) {
        this.datum = LocalDate.parse(datumStr, DATE_FORMATTER);
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.werkstatt = werkstatt;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public double getKosten() {
        return kosten;
    }

    public void setKosten(double kosten) {
        this.kosten = kosten;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getWerkstatt() {
        return werkstatt;
    }

    public void setWerkstatt(String werkstatt) {
        this.werkstatt = werkstatt;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getDatumFormatted() {
        return datum.format(DATE_FORMATTER);
    }
} 