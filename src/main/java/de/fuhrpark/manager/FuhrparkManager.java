// src/main/java/de/fuhrpark/manager/FuhrparkManager.java
package de.fuhrpark.manager;

import de.fuhrpark.model.base.Fahrzeug;
import de.fuhrpark.model.impl.PKW;
import de.fuhrpark.model.impl.LKW;
import java.util.ArrayList;
import java.util.List;

/**
 * Zentrale Verwaltungsklasse für den Fuhrpark.
 * Implementiert Dependency Injection für Services.
 */
public class FuhrparkManager {
    private final List<Fahrzeug> fahrzeuge;

    public FuhrparkManager() {
        this.fahrzeuge = new ArrayList<>();
    }

    public void addFahrzeug(Fahrzeug fahrzeug) {
        fahrzeuge.add(fahrzeug);
    }

    public List<Fahrzeug> getAlleFahrzeuge() {
        return new ArrayList<>(fahrzeuge);
    }

    public Fahrzeug createFahrzeug(String typ, String kennzeichen, String marke, String modell, double preis) {
        return switch (typ) {
            case "PKW" -> new PKW(kennzeichen, marke, modell, preis);
            case "LKW" -> new LKW(kennzeichen, marke, modell, preis);
            default -> throw new IllegalArgumentException("Unbekannter Fahrzeugtyp: " + typ);
        };
    }

    public void deleteFahrzeug(String kennzeichen) {
        fahrzeuge.removeIf(fahrzeug -> fahrzeug.getKennzeichen().equals(kennzeichen));
    }

    public Fahrzeug getFahrzeug(String kennzeichen) {
        return fahrzeuge.stream()
                .filter(fahrzeug -> fahrzeug.getKennzeichen().equals(kennzeichen))
                .findFirst()
                .orElse(null);
    }
}

