package de.fuhrpark.model.base;

public abstract class AbstractFahrzeug implements Fahrzeug {
    protected final FahrzeugTyp typ;
    protected final String marke;
    protected final String modell;
    protected final String kennzeichen;
    protected final double preis;

    protected AbstractFahrzeug(FahrzeugTyp typ, String marke, String modell, String kennzeichen, double preis) {
        this.typ = typ;
        this.marke = marke;
        this.modell = modell;
        this.kennzeichen = kennzeichen;
        this.preis = preis;
    }

    @Override
    public FahrzeugTyp getTyp() {
        return typ;
    }

    @Override
    public String getMarke() {
        return marke;
    }

    @Override
    public String getModell() {
        return modell;
    }

    @Override
    public String getKennzeichen() {
        return kennzeichen;
    }

    @Override
    public double getPreis() {
        return preis;
    }

    // Each vehicle type will implement its own depreciation calculation
    @Override
    public abstract double berechneAktuellenWert();
} 