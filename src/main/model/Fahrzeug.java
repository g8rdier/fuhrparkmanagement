// src/main/java/de/fuhrpark/model/Fahrzeug.java
package de.fuhrpark.model;

/**
 * Repräsentiert ein Fahrzeug im Fuhrpark.
 */
public class Fahrzeug {
    private String kennzeichen;
    private String modell;
    private int baujahr;
    private String status; // "verfügbar", "in Wartung", "ausgeliehen"

    // Konstruktor
    public Fahrzeug(String kennzeichen, String modell, int baujahr) {
        this.kennzeichen = kennzeichen;
        this.modell = modell;
        this.baujahr = baujahr;
        this.status = "verfügbar";
    }

    // Getter/Setter mit Javadoc
    /** @return Das Kennzeichen des Fahrzeugs (z. B. "K-ABC123"). */
    public String getKennzeichen() { return kennzeichen; }

    /** @param status Der neue Status ("verfügbar", "in Wartung"). */
    public void setStatus(String status) { this.status = status; }
    // ... weitere Getter/Setter
}
