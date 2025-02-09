package de.fuhrpark.model.base;

import de.fuhrpark.model.enums.FahrzeugTyp;

public abstract class AbstractFahrzeug implements Fahrzeug {
    private String marke;
    private String modell;
    private final String kennzeichen;
    private double preis;

    protected AbstractFahrzeug(String marke, String modell, String kennzeichen, double preis) {
        this.marke = marke;
        this.modell = modell;
        this.kennzeichen = kennzeichen;
        this.preis = preis;
    }

    @Override
    public String getMarke() {
        return marke;
    }

    @Override
    public void setMarke(String marke) {
        this.marke = marke;
    }

    @Override
    public String getModell() {
        return modell;
    }

    @Override
    public void setModell(String modell) {
        this.modell = modell;
    }

    @Override
    public String getKennzeichen() {
        return kennzeichen;
    }

    @Override
    public double getPreis() {
        return preis;
    }

    @Override
    public void setPreis(double preis) {
        this.preis = preis;
    }

    @Override
    public abstract FahrzeugTyp getTyp();

    // Each vehicle type will implement its own depreciation calculation
    @Override
    public abstract double berechneAktuellenWert();
} 