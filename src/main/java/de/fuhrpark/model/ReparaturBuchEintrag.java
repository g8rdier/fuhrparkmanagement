package de.fuhrpark.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReparaturBuchEintrag {
    private Long id;
    private String kennzeichen;
    private LocalDate datum;
    private String beschreibung;
    private double kosten;
    private String werkstatt;

    public ReparaturBuchEintrag(LocalDate datum, String beschreibung, double kosten, String werkstatt) {
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.werkstatt = werkstatt;
    }

    public ReparaturBuchEintrag(Long id, String kennzeichen, LocalDate datum, 
                               String beschreibung, double kosten, String werkstatt) {
        this.id = id;
        this.kennzeichen = kennzeichen;
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.kosten = kosten;
        this.werkstatt = werkstatt;
    }

    // Getters
    public Long getId() { return id; }
    public String getKennzeichen() { return kennzeichen; }
    public LocalDate getDatum() { return datum; }
    public String getBeschreibung() { return beschreibung; }
    public double getKosten() { return kosten; }
    public String getWerkstatt() { return werkstatt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setKennzeichen(String kennzeichen) { this.kennzeichen = kennzeichen; }
    public void setDatum(LocalDate datum) { this.datum = datum; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }
    public void setKosten(double kosten) { this.kosten = kosten; }
    public void setWerkstatt(String werkstatt) { this.werkstatt = werkstatt; }

    public String getDatumFormatted() {
        return datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
} 