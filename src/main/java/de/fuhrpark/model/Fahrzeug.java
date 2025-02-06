package de.fuhrpark.model;

import de.fuhrpark.model.enums.FahrzeugTyp;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Repräsentiert ein Fahrzeug im Fuhrpark.
 */
public class Fahrzeug {
    private String kennzeichen;
    private String marke;        // Added manufacturer
    private String modell;
    private FahrzeugTyp typ;     // PKW or LKW
    private int baujahr;
    private String status; // "verfügbar", "in Wartung", "ausgeliehen"
    private double aktuellerWert;
    private List<FahrtenbuchEintrag> fahrtenbuch;
    private List<ReparaturBuchEintrag> reparaturen;

    // Konstruktor
    public Fahrzeug(String kennzeichen, String marke, String modell, 
                   FahrzeugTyp typ, int baujahr, double initialerWert) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.typ = typ;
        this.baujahr = baujahr;
        this.aktuellerWert = initialerWert;
        this.status = "verfügbar";
        this.fahrtenbuch = new ArrayList<>();
        this.reparaturen = new ArrayList<>();
    }

    // Getter/Setter
    public String getKennzeichen() {
        return kennzeichen;
    }

    public String getMarke() {
        return marke;
    }

    public String getModell() {
        return modell;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FahrzeugTyp getTyp() {
        return typ;
    }

    public double getAktuellerWert() {
        return aktuellerWert;
    }

    public void setAktuellerWert(double wert) {
        this.aktuellerWert = wert;
    }

    public List<FahrtenbuchEintrag> getFahrtenbuch() {
        return new ArrayList<>(fahrtenbuch);
    }

    public List<ReparaturBuchEintrag> getReparaturen() {
        return new ArrayList<>(reparaturen);
    }

    public void addFahrtenbuchEintrag(FahrtenbuchEintrag eintrag) {
        fahrtenbuch.add(eintrag);
    }

    public void addReparatur(ReparaturBuchEintrag reparatur) {
        reparaturen.add(reparatur);
    }

    /**
     * Prüft, ob das Fahrzeug verfügbar ist.
     * @return true, wenn der Status "verfügbar" ist.
     */
    public boolean isVerfuegbar() {
        return "verfügbar".equals(this.status);
    }

    // Method to calculate depreciation
    public void berechneAbschreibung(double prozent) {
        this.aktuellerWert = this.aktuellerWert * (1 - prozent/100);
    }
}
