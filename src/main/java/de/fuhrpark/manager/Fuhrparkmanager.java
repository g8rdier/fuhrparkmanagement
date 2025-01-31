// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.Fahrzeug;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Verwaltet die Fahrzeuge des Fuhrparks.
 */
public class FuhrparkManager {
    private List<Fahrzeug> fahrzeuge = new ArrayList<>();

    /**
     * Fügt ein Fahrzeug hinzu.
     * @param fahrzeug Das hinzuzufügende Fahrzeug (nicht null).
     */
    public void addFahrzeug(Fahrzeug fahrzeug) {
        if (fahrzeug == null) throw new IllegalArgumentException("Fahrzeug darf nicht null sein");
        fahrzeuge.add(fahrzeug);
    }

    /**
     * Filtert Fahrzeuge nach einer Bedingung (Lambda).
     * @param bedingung Prädikat zur Filterung (z. B. f -> f.getStatus().equals("verfügbar")).
     * @return Gefilterte Liste.
     */
    public List<Fahrzeug> filterFahrzeuge(Predicate<Fahrzeug> bedingung) {
        return fahrzeuge.stream().filter(bedingung).toList();
    }
}
